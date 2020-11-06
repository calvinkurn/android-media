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
            severity = Severity.WARNING,
            implementation = SourceCodeDetector.IMPLEMENTATION

    ).setAndroidSpecific(true)

    var applicableMethodNames = MethodClassRuleManager.methodClassNameMapping.keys.toList()
    fun checkMethod(context: JavaContext,
                    node: UCallExpression,
                    method: PsiMethod) {

        if (MethodClassRuleManager.checkMethodContaingClass(method.containingClass?.qualifiedName
                        ?: "", methodName = method.name)) {
            messageString(method = method.name, expectedClassMethod = MethodClassRuleManager.getExpectedMethodClass(method.containingClass?.qualifiedName
                    ?: "", method.name))?.let {
                reportIssue(context, node, it)
            }

        }
    }

    private fun reportIssue(
            context: JavaContext,
            node: UCallExpression,
            message: String
    ) {

        context.report(
                issue = METHOD_CALL_PROHIBITED_ISSUE,
                location = context.getCallLocation(call = node, includeReceiver = false, includeArguments = false),
                message = message
        )
    }

    fun messageString(method: String,
                      expectedClassMethod: MethodClassMapper?): String? {

        expectedClassMethod ?: return null
        return "Usage of `${method}is prohibited, Use ${expectedClassMethod.className}.${expectedClassMethod.methodName}"
    }

}