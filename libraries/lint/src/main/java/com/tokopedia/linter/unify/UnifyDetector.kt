package com.tokopedia.linter.unify

import com.android.SdkConstants
import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.UNIFY_USAGE_ISSUE_ID
import com.tokopedia.linter.detectors.xml.XMLDetector
import org.w3c.dom.Element

object UnifyDetector {
    val unifyMap = getUnifyMapping();

    fun getUnifyMapping(): Map<String, UnifyMapping> {
        return mapOf(SdkConstants.TEXT_VIEW to UnifyMapping(SdkConstants.TEXT_VIEW, UnifyComponentsList.TYPOGRAPHY))
    }


    val ISSUE: Issue = Issue.create(
            UNIFY_USAGE_ISSUE_ID,
            "Widget replaced with UnifyComponent",
            "We have Unify Components Tokopedia Recommend to use them",
            Category.CORRECTNESS,
            3,
            Severity.WARNING,
            XMLDetector.IMPLEMENTATION)

    fun checkUnify(context: XmlContext, element: Element) {
        unifyMap[element.tagName]?.let {
            LintFix.create().name(unifyMap[element.tagName]?.getMessage())
                    .replace()
                    .text(element.tagName)
                    .with(unifyMap[element.tagName]?.newName)
                    .robot(true)
                    .independent(true)
                    .build().apply {

                        context.report(ISSUE, location = context.getLocation(element), message = it.getMessage(), quickfixData = this)
                    }

        }
    }
}
