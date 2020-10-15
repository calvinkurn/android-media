package com.tokopedia.linter.detectors

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.tokopedia.linter.*
import org.jetbrains.uast.UCallExpression

class DialogUnifyDetector : Detector(), SourceCodeScanner {

    companion object {
        private val IMPLEMENTATION = Implementation(
                DialogUnifyDetector::class.java,
                Scope.JAVA_FILE_SCOPE
        )
        private val DIALOG_APPLICABLE_METHODS_LIST = listOf("setTitle", "onCancelListener", "setCancelMessage", "setCancelable")


        val ISSUE = Issue.create(
                DIALOG_ISSUE_ID,
                WARNING_DESCRIPTION_FORMAT.format(DIALOG_OLD_NAME, DIALOG_NEW_NAME),
                WARNING_EXPLANATION_FORMAT.format(DIALOG_OLD_NAME, DIALOG_NEW_NAME),
                Category.CORRECTNESS,
                6,
                Severity.ERROR,
                IMPLEMENTATION
        )
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        super.visitMethodCall(context, node, method)
        val evaluator = context.evaluator
        if (evaluator.isMemberInClass(method, DIALOG_CLASS_NAME))
            context.report(ISSUE, context.getLocation(node), USAGE_ERROR_MESSAGE_FORMAT.format(DIALOG_NEW_NAME))
    }

    override fun getApplicableMethodNames(): List<String> {
        return DIALOG_APPLICABLE_METHODS_LIST
    }

}
