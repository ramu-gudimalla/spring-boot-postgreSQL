package springtutorial.io.springpostgreSQL.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private final StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public List<Student> getStudents(){
        return studentRepository.findAll();
    }
    public void addNewStudent(Student student){
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()){
            throw new IllegalStateException("Email already taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exits = studentRepository.existsById(studentId);
        if (!exits){
            throw new IllegalStateException("Student with id "+studentId+" doesn't exists");
        }
        studentRepository.deleteById(studentId);
    }
    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
//        boolean exists = studentRepository.existsById(studentId);
//        if (!exists){
//            throw new IllegalStateException("Student with id "+studentId+" doesn't exist");
//        }
        Student student = studentRepository.findById(studentId).orElseThrow(()-> new IllegalStateException("Student with id "+studentId+" doesn't exist"));
        if(name != null && !name.isEmpty() && !Objects.equals(student.getName(),name)){
            student.setName(name);
        }
        if(email != null && !email.isEmpty() && !Objects.equals(student.getEmail(),email)){
            Optional <Student> studentOptional = studentRepository.findStudentByEmail(email);
            if(studentOptional.isPresent()){
                throw new IllegalStateException("Email taken");
            }
            student.setEmail(email);
        }
    }
}
