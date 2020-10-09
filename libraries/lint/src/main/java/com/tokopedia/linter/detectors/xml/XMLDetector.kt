package com.tokopedia.linter.detectors.xml

import com.android.SdkConstants
import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.*
import com.tokopedia.linter.detectors.FloatingButtonUnifyDetector
import org.w3c.dom.Element

class XMLDetector : Detector(), XmlScanner {


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


        context.report(FloatingButtonUnifyDetector.ISSUE, location = context.getElementLocation(element),
                message = USAGE_ERROR_MESSAGE_FORMAT.format(FLOATING_BUTTON_FIX_SHORT_NAME), quickfixData = floatingButtonUnify)

        val materialFloatingButtonUnify = LintFix.create().name(FIX_NAME_FORMAT.format(FLOATING_BUTTON_FIX_SHORT_NAME))
                .replace()
                .text(FLOATING_ACTION_BUTTON_OLD_NAME)
                .with(FLOATING_BUTTON_NEW_NAME)
                .robot(true)
                .independent(true)
                .build()

        context.report(FloatingButtonUnifyDetector.ISSUE, location = context.getElementLocation(element),
                message = USAGE_ERROR_MESSAGE_FORMAT.format(FLOATING_BUTTON_FIX_SHORT_NAME), quickfixData = materialFloatingButtonUnify)
    }
}