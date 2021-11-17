package com.tokopedia.linter.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UElement
import org.jetbrains.uast.kotlin.KotlinUParameter

class ExposeAnnotationDetector : Detector(), SourceCodeScanner {

    companion object {
        val ISSUE = Issue.create(
            id = "ExposeAnnotation",
            briefDescription = "Response field without @Expose annotation can cause data parsing error.",
            explanation = "Add @Expose annotation to avoid data parsing error.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ExposeAnnotationDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        private const val SERIALIZED_NAME_ANNOTATION = "com.google.gson.annotations.SerializedName"
        private const val EXPOSE_ANNOTATION = "com.google.gson.annotations.Expose"
        private const val MESSAGE = "Response field without @Expose annotation can cause data parsing error. " +
            "Add @Expose annotation to avoid error."
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        return listOf(UAnnotation::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitAnnotation(node: UAnnotation) {
                if(node.qualifiedName == SERIALIZED_NAME_ANNOTATION) {
                    val param = node.uastParent
                    val uParam = param as KotlinUParameter
                    val exposeAnnotation = uParam.findAnnotation(EXPOSE_ANNOTATION)
                    if(exposeAnnotation == null) {
                        reportIssue(context, param)
                    }
                }
            }
        }
    }

    private fun reportIssue(context: JavaContext, node: UElement) {
        context.report(
            ISSUE,
            node,
            context.getNameLocation(node),
            MESSAGE
        )
    }
}