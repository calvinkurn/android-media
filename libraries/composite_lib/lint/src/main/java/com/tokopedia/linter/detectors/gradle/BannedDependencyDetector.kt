package com.tokopedia.linter.detectors.gradle

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.GradleContext
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Severity
import com.tokopedia.linter.LinterConstants
import com.tokopedia.linter.Priority

object BannedDependencyDetector {
    private const val ISSUE_GRADLE_NOT_ALLOWED = "GradleNotAllowed"
    private const val ISSUE_GRADLE_NOT_ALLOWED_BRIEF = "Not Allowed Gradle Construct"
    private const val ISSUE_GRADLE_NOT_ALLOWED_EXPLAINATION = "This detector looks for Banned Gradle constructs which currently work but we are not allow them to use in app"


    private val bannedList = mapOf(LinterConstants.Libraries.KOTLIN_REFLECTION to "")

    val DEPENDENCY_BANNED = Issue.create(
            ISSUE_GRADLE_NOT_ALLOWED,  //$NON-NLS-1$
            ISSUE_GRADLE_NOT_ALLOWED_BRIEF,
            ISSUE_GRADLE_NOT_ALLOWED_EXPLAINATION,
            Category.CORRECTNESS,
            Priority.High.value,
            Severity.ERROR,
            GradleDetector.IMPLEMENTATION
    )

    internal fun checkBannedDependencies(
            configuration: String,
            context: GradleContext,
            value: String,
            cookie: Any,
            dependency: String
    ) {
        if (bannedList.keys.contains(dependency)) {
            context.report(
                    DEPENDENCY_BANNED, context.getLocation(cookie), getBannedMessage(
                    dependency,
                    bannedList[dependency] ?: ""
            )
            )
        }
    }

    private fun getBannedMessage(libName: String, solution: String): String {
        return "Usage to ${libName} is Banned Please remove this" + if (solution.isNotEmpty()) "Replace with ${solution}" else ""
    }


}