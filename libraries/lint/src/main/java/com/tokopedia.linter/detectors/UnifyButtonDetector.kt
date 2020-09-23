package com.tokopedia.linter.detectors

import com.android.SdkConstants
import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.*
import org.w3c.dom.Element

class UnifyButtonDetector : Detector(), XmlScanner {
    companion object {
        private val IMPLEMENTATION = Implementation(
                UnifyButtonDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
        )

        val ISSUE: Issue = Issue.create(
                BUTTON_ISSUE_ID,
                WARNING_DESCRIPTION_FORMAT.format(BUTTON_OLD_NAME, BUTTON_NEW_NAME_SHORT),
                WARNING_EXPLANATION_FORMAT.format(BUTTON_OLD_NAME, BUTTON_NEW_NAME_SHORT),
                Category.CORRECTNESS,
                3,
                Severity.WARNING,
                IMPLEMENTATION)
    }

    override fun getApplicableElements(): Collection<String>? {
        return listOf(SdkConstants.BUTTON)
    }

    override fun visitElement(context: XmlContext, element: Element) {
        val unifyButton = LintFix.create().name(FIX_NAME_FORMAT.format(BUTTON_NEW_NAME_SHORT))
                .replace()
                .text(BUTTON_OLD_NAME)
                .with(BUTTON_NEW_NAME)
                .robot(true)
                .independent(true)
                .build()

        context.report(ISSUE, location = context.getLocation(element),
                message = USAGE_ERROR_MESSAGE_FORMAT.format(BUTTON_NEW_NAME_SHORT), quickfixData = unifyButton)

    }
}