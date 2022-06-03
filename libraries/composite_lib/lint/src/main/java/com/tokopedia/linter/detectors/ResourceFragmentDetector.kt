package com.tokopedia.linter.detectors

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

class ResourceFragmentDetector: Detector(), SourceCodeScanner {

    companion object {
        val ISSUE = Issue.create(
            id = "ResourceFragmentDetector",
            briefDescription = "Unsafe resource usage.",
            explanation = "Using resource directly from fragment can lead to crash. " +
                            "Because the type of resource is non-null and probably fragment is not being attached to the activity. " +
                            "Please use resource from context or activity instead. " +
                            "Example: getContext().getResources() in Java or context?.resources in Kotlin.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.ERROR,
            implementation = Implementation(ResourceFragmentDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        private const val METHOD_GET_RESOURCES = "getResources"
        private const val CLASS_NAME = "androidx.fragment.app.Fragment"
        private const val ERROR_MESSAGE = "Using resource directly from fragment can lead to crash. " +
                "Because the type of resource is non-null and probably fragment is not being attached to the activity. " +
                "Please use resource from context or activity instead. " +
                "Example: getContext().getResources() in Java or context?.resources in Kotlin."
    }

    override fun getApplicableMethodNames(): List<String> = listOf(METHOD_GET_RESOURCES)

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        scanMethodUsage(context, node, method)
    }

    private fun scanMethodUsage(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (context.evaluator.isMemberInClass(method, CLASS_NAME)) {
            reportUsage(context, node)
        }
    }

    private fun reportUsage(context: JavaContext, node: UCallExpression) {
        context.report(
            issue = ISSUE,
            scope = node,
            location = context.getCallLocation(
                call = node,
                includeReceiver = true,
                includeArguments = true
            ),
            message = ERROR_MESSAGE
        )
    }

}