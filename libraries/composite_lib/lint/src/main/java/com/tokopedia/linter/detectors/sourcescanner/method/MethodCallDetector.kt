package com.tokopedia.linter.detectors.sourcescanner.method

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.tokopedia.linter.Priority
import com.tokopedia.linter.detectors.sourcescanner.SourceCodeDetector
import org.jetbrains.uast.UCallExpression

object MethodCallDetector {

    val METHOD_CALL_ISSUE_ID = "Method Call Prohibited"
    val BRIEF_DESCRIPTION = "Please check method Call"
    val EXPLAINATION = "We have alternative method call implementation that we suggest to use"

    val METHOD_CALL_PROHIBITED_ISSUE: Issue = Issue.create(
        id = METHOD_CALL_ISSUE_ID,
        briefDescription = BRIEF_DESCRIPTION,
        explanation = EXPLAINATION,
        category = Category.CORRECTNESS,
        priority = Priority.Low.value,
        severity = Severity.ERROR,
        implementation = SourceCodeDetector.IMPLEMENTATION

    ).setAndroidSpecific(true)

    var applicableMethodNames = MethodClassRuleManager.methodClassNameMapping.keys.toList()

    fun checkMethod(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {

        if (MethodClassRuleManager.checkMethodContaingClass(
                method.containingClass?.qualifiedName
                    ?: "", methodName = method.name
            )
        ) {
            val expectedClassMethod = MethodClassRuleManager.getExpectedMethodClass(
                method.containingClass?.qualifiedName ?: "", method.name
            )
            expectedClassMethod?.let {
                reportIssue(context, node, (it.errorMsg ?: messageString(method = method.name, it)))
            }

        }
    }

    private fun reportIssue(
        context: JavaContext,
        node: UCallExpression,
        message: String,
        lintfix: LintFix? = null
    ) {


        context.report(
            issue = METHOD_CALL_PROHIBITED_ISSUE,
            location = context.getCallLocation(
                call = node,
                includeReceiver = false,
                includeArguments = false
            ),
            message = message,
            quickfixData = lintfix
        )
    }

    fun messageString(
        method: String,
        expectedClassMethod: MethodClassMapper
    ): String {
        return "Usage of `${method} is prohibited, Use ${expectedClassMethod.className}.${expectedClassMethod.methodName}"
    }

}
