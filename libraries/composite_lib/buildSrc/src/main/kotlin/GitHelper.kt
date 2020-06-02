import com.tokopedia.plugin.runCommand
import com.tokopedia.plugin.trimSpecial
import org.gradle.api.Project

const val botuser = "jenkins"

fun getCommitId(rootProject: Project, projectName: String): String {
    val gitLogEmail = "git log -3 --pretty=format:\'%ae\' ${projectName}"
    val gitLogCommitId = "git log -3 --pretty=format:\'%h\' ${projectName}"
    val gitLogEmailResult = (gitLogEmail.runCommand(rootProject.projectDir.absoluteFile)?.trimSpecial() ?: "").split("\n")
    val gitLogCommitIdResult = (gitLogCommitId.runCommand(rootProject.projectDir.absoluteFile)?.trimSpecial() ?: "").split("\n")
    println(gitLogEmailResult.toString())
    println(gitLogCommitIdResult.toString())
    try {
        if (gitLogEmailResult.isNotEmpty()) {
            val userIndex = getUserIndex(gitLogEmailResult)
            if (userIndex == -1) {
                return ""
            } else {
                return gitLogCommitIdResult[userIndex]
            }
        } else {
            return ""
        }
    } catch (e: Exception) {
        return ""
    }
}

fun getUserIndex(logEmailList:List<String>):Int {
    for ((index, email) in logEmailList.withIndex()) {
        if (email.contains(botuser)) {
            continue
        } else {
            return index
        }
    }
    return -1
}