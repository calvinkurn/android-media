package com.tokopedia.linter.detectors.gradle

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.GradleContext
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Severity
import com.tokopedia.linter.LinterConstants
import com.tokopedia.linter.Priority


object DeprecatedDependencyDetector {

    private const val ISSUE_GRADLE_DEPRECATED = "GradleNotAllowed"
    private const val ISSUE_GRADLE_DEPRECATED_BRIEF = "Deprecated Gradle Construct"
    private const val ISSUE_GRADLE_DEPRECATED_EXPLANATION = "This detector looks for deprecated Gradle constructs which currently work but will likely stop working in a future update."

    val deprecatedList = mapOf(GradleDetector.getStringDependency(LinterConstants.Libraries.TKPD_DESIGN) to "",
            LinterConstants.Libraries.TKPD_DESIGN_LIBRARIES to "")

    /** Deprecated Gradle constructs  */
    val DEPENDENCY_DEPRECATED = Issue.create(
            ISSUE_GRADLE_DEPRECATED,  //$NON-NLS-1$
            ISSUE_GRADLE_DEPRECATED_BRIEF,
            ISSUE_GRADLE_DEPRECATED_EXPLANATION,
            Category.CORRECTNESS,
            Priority.Medium.value,
            Severity.ERROR,
            GradleDetector.IMPLEMENTATION
    )

    internal fun checkDeprecatedDependencies(
            configuration: String,
            context: GradleContext,
            value: String,
            cookie: Any,
            dependency: String
    ) {

        if (deprecatedList.keys.contains(dependency)) {
            context.report(
                    DEPENDENCY_DEPRECATED, context.getLocation(cookie), getDeprecatedMessage(
                    dependency,
                    deprecatedList[dependency] ?: ""
            )
            )
        }

    }

    fun getDeprecatedMessage(libName: String, solution: String): String {
        return "Usage to ${libName} is deprecated Please remove this" + if (solution.isNotEmpty()) "Replace with ${solution}" else ""
    }
}

