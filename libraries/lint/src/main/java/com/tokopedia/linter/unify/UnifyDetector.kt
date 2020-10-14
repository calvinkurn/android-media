package com.tokopedia.linter.unify

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.tokopedia.linter.UNIFY_USAGE_ISSUE_ID
import com.tokopedia.linter.detectors.xml.XMLDetector
import com.tokopedia.linter.unify.UnifyComponentsList.getUnifyMapping
import org.jetbrains.uast.UCallExpression
import org.w3c.dom.Element

object UnifyDetector {
    const val BRIEF_DESCRIPTION = "Widget replaced with UnifyComponent";
    const val EXPLANATION = "We have Unify Components Tokopedia Recommend to use them"


    val unifyMap = getUnifyMapping();

    val ISSUE: Issue = Issue.create(
            UNIFY_USAGE_ISSUE_ID,
            BRIEF_DESCRIPTION,
            EXPLANATION,
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

    fun checkUnify(context: JavaContext,
                   node: UCallExpression,
                   constructor: String) {
        unifyMap[constructor]?.let {
            LintFix.create().name(unifyMap[constructor]?.getMessage())
                    .replace()
                    .text(constructor)
                    .with(unifyMap[constructor]?.newName)
                    .robot(true)
                    .independent(true)
                    .build().apply {
                        context.report(ISSUE, location = context.getLocation(node), message = it.getMessage(), quickfixData = this)
                    }

        }
    }
}
