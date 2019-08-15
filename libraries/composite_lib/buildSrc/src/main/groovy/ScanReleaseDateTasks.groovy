import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

class ScanReleaseDateTasks extends DefaultTask{
    def listReleaseDate = []

    @TaskAction
    def scanReleaseDateProcess(){
        println "scanInput Proses"
        new File('tools/version/release_date.txt').eachLine { line ->
            println "scanInput Proses"
            listReleaseDate.add(line)
        }
    }


}
