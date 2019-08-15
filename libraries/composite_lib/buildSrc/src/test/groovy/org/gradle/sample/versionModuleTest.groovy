
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

    def setup() {
        settingsFile = testProjectDir.newFile('settings.gradle')
        buildFile = testProjectDir.newFile('build.gradle')
    }

    @Unroll
    def "can execute scan release date task"() {
        given:
        buildFile << """
            task scanReleaseDate(){
                doLast{
                    logger.quiet "scanInput Proses"
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
}

