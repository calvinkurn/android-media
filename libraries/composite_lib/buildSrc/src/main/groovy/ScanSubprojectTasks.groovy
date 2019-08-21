import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ScanSubprojectTasks extends DefaultTask{
    def listModule = []

    @TaskAction
    void scanSubProject(){
        project.subprojects.each { p ->
            listModule.add(p.path.substring(1))
        }
    }

}