@file:Suppress("UnstableApiUsage")

package com.tokopedia.linter.unify

import com.android.SdkConstants
import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.detectors.xml.XMLDetector
import com.tokopedia.linter.unify.UnifyComponentsList.getUnifyMapping
import com.tokopedia.linter.unify.UnifyComponentsList.widgetViewMapping
import org.jetbrains.uast.UCallExpression
import org.w3c.dom.Element

object UnifyDetector {
    const val UNIFY_USAGE_ISSUE_ID = "UnifyComponentUsage"
    const val UNIFY_USAGE_BRIEF_DESCRIPTION = "Widget replaced with UnifyComponent";
    const val UNIFY_USAGE_EXPLANATION = "We have Unify Components Tokopedia Recommend to use them"


    val unifyMapKeys = (widgetViewMapping +  getUnifyMapping());

    val ISSUE: Issue = Issue.create(
            UNIFY_USAGE_ISSUE_ID,
            UNIFY_USAGE_BRIEF_DESCRIPTION,
            UNIFY_USAGE_EXPLANATION,
            Category.CORRECTNESS,
            3,
            Severity.WARNING,
            XMLDetector.IMPLEMENTATION)

    fun checkUnify(context: XmlContext, element: Element) {
        unifyMapKeys[element.tagName]?.let {
            LintFix.create().name(unifyMapKeys[element.tagName]?.getMessage())
                    .replace()
                    .text(element.tagName)
                    .with(unifyMapKeys[element.tagName]?.newName)
                    .robot(true)
                    .independent(true)
                    .build().apply {

                        context.report(ISSUE, location = context.getLocation(element), message = it.getMessage(), quickfixData = this)
                    }

        }
    }

    fun checkUnify(context: JavaContext,
                   node: UCallExpression,
                   constructor_name: String) {
        var constructor = constructor_name
        System.out.println(constructor)
        constructor = if (constructor.startsWith(SdkConstants.WIDGET_PKG_PREFIX)) {
            constructor.substring(constructor.lastIndexOf('.')+1)
        }else {
            constructor
        }
        unifyMapKeys[constructor]?.let {
            LintFix.create().name(unifyMapKeys[constructor]?.getMessage())
                    .replace()
                    .text(constructor)
                    .with(unifyMapKeys[constructor]?.newName)
                    .robot(true)
                    .independent(true)
                    .build().apply {
                        context.report(ISSUE, location = context.getLocation(node), message = it.getMessage(), quickfixData = this)
                    }

        }
    }
}
