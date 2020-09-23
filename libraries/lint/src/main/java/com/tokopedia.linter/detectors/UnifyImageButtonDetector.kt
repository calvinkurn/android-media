package com.tokopedia.linter.detectors

import com.android.SdkConstants
import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.*
import org.w3c.dom.Element

class UnifyImageButtonDetector : Detector(), XmlScanner {

    companion object {
        private val implementation = Implementation(
                UnifyImageButtonDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
        )

        val ISSUE = Issue.create(
                IMAGE_BUTTON_ISSUE_ID,
                WARNING_DESCRIPTION_FORMAT.format(IMAGE_BUTTON_OLD_SHORT_NAME, IMAGE_BUTTON_NEW_SHORT_NAME),

                WARNING_EXPLANATION_FORMAT.format(IMAGE_BUTTON_OLD_SHORT_NAME, IMAGE_BUTTON_NEW_SHORT_NAME),
                Category.CORRECTNESS,
                3,
                Severity.WARNING,
                implementation
        )
    }

    override fun getApplicableElements(): Collection<String>? {
        return listOf(SdkConstants.IMAGE_BUTTON)
    }

    override fun visitElement(context: XmlContext, element: Element) {
        val unifyImageButton = LintFix.create().name(FIX_NAME_FORMAT.format(IMAGE_BUTTON_NEW_SHORT_NAME))
                .replace()
                .text(IMAGE_BUTTON_OLD_SHORT_NAME)
                .with(IMAGE_BUTTON_NEW_NAME)
                .robot(true)
                .independent(true)
                .build()

        context.report(ISSUE, location = context.getElementLocation(element),
                message = USAGE_ERROR_MESSAGE_FORMAT.format(IMAGE_BUTTON_NEW_SHORT_NAME), quickfixData = unifyImageButton)
    }
}