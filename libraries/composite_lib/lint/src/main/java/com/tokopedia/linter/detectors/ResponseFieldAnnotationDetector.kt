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
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UParameter
import org.jetbrains.uast.getContainingUClass
import org.jetbrains.uast.kotlin.KotlinConstructorUMethod

class ResponseFieldAnnotationDetector : Detector(), SourceCodeScanner {

    companion object {
        val ISSUE = Issue.create(
            id = "ResponseFieldAnnotation",
            briefDescription = "Response field without @SerializedName and @Expose annotation can cause data parsing error.",
            explanation = "Response field without @SerializedName and @Expose annotation can cause data parsing error. " +
                "Add @SerializedName and @Expose annotation to avoid error.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ResponseFieldAnnotationDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        private const val RESPONSE_KEYWORD = "Response"
        private const val PARAM_KEYWORD = "Param"
        private const val DOMAIN_MODEL_PATH= "domain/model"
        private const val SERIALIZED_NAME_ANNOTATION = "com.google.gson.annotations.SerializedName"
        private const val EXPOSE_ANNOTATION = "com.google.gson.annotations.Expose"
        private const val MESSAGE = "Response field \"%s\" without @SerializedName and @Expose annotation can cause data parsing error. " +
            "Add @SerializedName and @Expose annotation to avoid error."
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UParameter::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitParameter(node: UParameter) {
                val isDataClass = node.getContainingUClass()?.methods?.map { it.name }
                    ?.containsAll(listOf("equals", "hashCode")) == true
                val serializedNameAnnotation = node.findAnnotation(SERIALIZED_NAME_ANNOTATION)
                val exposeAnnotation = node.findAnnotation(EXPOSE_ANNOTATION)

                if(shouldCheckAnnotation(context, node) && isDataClass &&
                    (serializedNameAnnotation == null || exposeAnnotation == null)) {
                    reportError(context, node)
                }
            }
        }
    }

    private fun shouldCheckAnnotation(context: JavaContext, node: UParameter): Boolean {
        val fileName = context.file.name
        val filePath = context.file.path
        return (fileName.contains(RESPONSE_KEYWORD) || fileName.contains(PARAM_KEYWORD) ||
                filePath.contains(DOMAIN_MODEL_PATH)) && node.uastParent is KotlinConstructorUMethod
    }

    private fun reportError(context: JavaContext, node: UElement) {
        val fieldName = (node.sourcePsi as? KtParameter)?.name.orEmpty()
        val message = String.format(MESSAGE, fieldName)

        context.report(
            ISSUE,
            node,
            context.getNameLocation(node),
            message
        )
    }
}