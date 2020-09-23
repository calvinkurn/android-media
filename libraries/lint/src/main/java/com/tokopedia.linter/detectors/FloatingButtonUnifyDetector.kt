package com.tokopedia.linter.detectors

import com.android.SdkConstants
import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.*
import org.w3c.dom.Element

class FloatingButtonUnifyDetector : Detector(), XmlScanner {

    companion object {
        private val IMPLEMENTATION = Implementation(
                FloatingButtonUnifyDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
        )

        val ISSUE: Issue = Issue.create(
                FLOATING_BUTTON_ISSUE_ID,
                WARNING_DESCRIPTION_FORMAT.format(FLOATING_BUTTON_ACTUAL_SHORT_NAME, FLOATING_BUTTON_FIX_SHORT_NAME),
                WARNING_EXPLANATION_FORMAT.format(FLOATING_BUTTON_ACTUAL_SHORT_NAME, FLOATING_BUTTON_FIX_SHORT_NAME),
                Category.CORRECTNESS,
                3,
                Severity.WARNING,
                IMPLEMENTATION)
    }

    override fun getApplicableElements(): Collection<String>? {
        return listOf(SdkConstants.FLOATING_ACTION_BUTTON.defaultName())
    }

    override fun visitElement(context: XmlContext, element: Element) {
        val floatingButtonUnify = LintFix.create().name(FIX_NAME_FORMAT.format(FLOATING_BUTTON_FIX_SHORT_NAME))
                .replace()
                .text(FLOATING_BUTTON_OLD_NAME)
                .with(FLOATING_BUTTON_NEW_NAME)
                .robot(true)
                .independent(true)
                .build()


        context.report(ISSUE, location = context.getElementLocation(element),
                message = USAGE_ERROR_MESSAGE_FORMAT.format(FLOATING_BUTTON_FIX_SHORT_NAME), quickfixData = floatingButtonUnify)

        val materialFloatingButtonUnify = LintFix.create().name(FIX_NAME_FORMAT.format(FLOATING_BUTTON_FIX_SHORT_NAME))
                .replace()
                .text(FLOATING_ACTION_BUTTON_OLD_NAME)
                .with(FLOATING_BUTTON_NEW_NAME)
                .robot(true)
                .independent(true)
                .build()

        context.report(ISSUE, location = context.getElementLocation(element),
                message = USAGE_ERROR_MESSAGE_FORMAT.format(FLOATING_BUTTON_FIX_SHORT_NAME), quickfixData = materialFloatingButtonUnify)
    }

}