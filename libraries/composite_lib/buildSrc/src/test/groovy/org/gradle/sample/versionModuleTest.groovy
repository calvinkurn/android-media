
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Unroll

import static org.gradle.testkit.runner.TaskOutcome.*
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class versionModuleTest extends Specification {
    @Rule TemporaryFolder testProjectDir = new TemporaryFolder()
    File settingsFile
    File buildFile
    File releaseDateFile

    def  treeModel
    def  versionModel
    Graph graph

    def setup() {
        settingsFile = testProjectDir.newFile('settings.gradle')
        buildFile = testProjectDir.newFile('build.gradle')
        releaseDateFile = testProjectDir.newFile('release_date.txt')
    }
    @Unroll
    def "test tree model"() {
        when:
        treeModel = new TreeModel("test1","test2")

        then:
        assert treeModel.first=="test1"
        assert treeModel.second=="test2"
    }

    @Unroll
    def "test version model"() {
        when:
        versionModel = new VersionModel("project",3, "com.tokopedia.graphql", "graphql","graphql")

        then:
        assert versionModel.project=="project"
        assert versionModel.version==3
        assert versionModel.artifactId=="graphql"
        assert versionModel.artifactName=="graphql"
        assert versionModel.groupId=="com.tokopedia.graphql"
    }

    @Unroll
    def "test graph toposort"() {
        when:
        graph = new Graph(6)
        graph.addEdge(5, 2);
        graph.addEdge(5, 0);
        graph.addEdge(4, 0);
        graph.addEdge(4, 1);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);
        graph.topologicalSort()

        then:
        def answer = [5, 4, 2, 3, 1, 0]
        int i = 0
        graph.listTop.each {
            assert it == answer[i]
            i++
        }
    }

    @Unroll
    def "can execute scan release date task"() {
        given:
        settingsFile << "rootProject.name = 'test'"
        releaseDateFile << """
        2019-06-09 00:00:00\n
        2019-07-26 00:00:00
        """

        buildFile << """
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

        task scanReleaseDate(type: ScanReleaseDateTasks){
            fileDir = '${releaseDateFile.path}'
            doLast{
                println "scanInput Proses"
            }
        }
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('scanReleaseDate')
                .build()

        then:
        result.output.contains('scanInput Proses')
        result.task(":scanReleaseDate").outcome == SUCCESS
    }

    @Unroll
    def "can execute scan subproject task"() {
        given:
        settingsFile << "rootProject.name = 'test'"

        buildFile << """
        class ScanSubprojectTasks extends DefaultTask{
            def listModule = []
        
            @TaskAction
            void scanSubProject(){
                project.subprojects.each { p ->
                    listModule.add(p.path.substring(1))
                }
            }
        
        }
        task scanSubproject(type: ScanSubprojectTasks){}
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('scanSubproject')
                .build()

        then:
        result.task(":scanSubproject").outcome == SUCCESS
    }


}

