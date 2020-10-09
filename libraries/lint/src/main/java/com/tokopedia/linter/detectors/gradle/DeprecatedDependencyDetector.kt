package com.tokopedia.linter.detectors.gradle

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.GradleContext
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Severity

val deprecatedList = mapOf(GradleDetector.getStringDependency("tkpddesign") to "",
        "rootProject.ext.libraries.tkpddesign" to "")

/** Deprecated Gradle constructs  */
val DEPENDENCY_DEPRECATED = Issue.create(
        "",  //$NON-NLS-1$
        "Deprecated Gradle Construct",
        "This detector looks for deprecated Gradle constructs which currently work but " +
                "will likely stop working in a future update.",
        Category.CORRECTNESS,
        6,
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

