import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ScanReleaseDateTasks extends DefaultTask{
    def listReleaseDate = []
    String fileDir

    @TaskAction
    def scanReleaseDateProcess(){
        new File(fileDir).eachLine { line ->
            listReleaseDate.add(line)
        }
    }
}
