package com.tokopedia.linter.detectors

import com.android.SdkConstants.TEXT_VIEW
import com.android.tools.lint.detector.api.*
import org.w3c.dom.Element

@Suppress("UnstableApiUsage")
class TypographyDetector : Detector(), XmlScanner {


    companion object {
        private val IMPLEMENTATION = Implementation(
                TypographyDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
        )

        val ISSUE: Issue = Issue.create(
                "TypographyOverTextViewUsage",
                "TextView should probably be Typography",
                """
                Using a `<TextView>` is not recommended, you should be using `<Typography>` instead.
                """.trimIndent(),
                Category.CORRECTNESS,
                3,
                Severity.WARNING,
                IMPLEMENTATION)
    }


    override fun getApplicableElements(): Collection<String>? {
        return listOf(
                TEXT_VIEW)
    }


    override fun visitElement(context: XmlContext, element: Element) {
        val typography = LintFix.create().name("Use Typography")
                .replace()
                .text("TextView")
                .with("com.tokopedia.unifyprinciples.Typography")
                .robot(true)
                .independent(true)
                .build()

        context.report(ISSUE, location = context.getLocation(element), message = "consider using Typography", quickfixData = typography)

    }
}