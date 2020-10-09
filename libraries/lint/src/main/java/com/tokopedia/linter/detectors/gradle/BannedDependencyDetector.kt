package com.tokopedia.linter.detectors.gradle

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.GradleContext
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Severity


val bannedList = mapOf("rootProject.ext.supportLibDependencies.kotlinReflection" to "")

val DEPENDENCY_BANNED = Issue.create(
        "GradleNotAllowed",  //$NON-NLS-1$
        "Not Allowed Gradle Construct",
        "This detector looks for Banned Gradle constructs which currently work but " +
                "we are not allow them to use in app",
        Category.CORRECTNESS,
        6,
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
                deprecatedList[dependency] ?: ""
        )
        )
    }
}

private fun getBannedMessage(libName: String, solution: String): String {
    return "Usage to ${libName} is Banned Please remove this" + if (solution.isNotEmpty()) "Replace with ${solution}" else ""
}