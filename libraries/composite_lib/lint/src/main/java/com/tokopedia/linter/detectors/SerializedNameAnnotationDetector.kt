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

class SerializedNameAnnotationDetector : Detector(), SourceCodeScanner {

    companion object {
        val RESPONSE_ISSUE = Issue.create(
            id = "ResponseFieldAnnotation",
            briefDescription = "Field without @SerializedName and @Expose annotation can cause data parsing error.",
            explanation = "Field without @SerializedName and @Expose annotation can cause data parsing error. " +
                "Add @SerializedName and @Expose annotation to avoid error.",
            category = Category.CORRECTNESS,
            priority = 10,
            severity = Severity.WARNING,
            implementation = Implementation(
                SerializedNameAnnotationDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val ENTITY_ISSUE = Issue.create(
            id = "EntityFieldAnnotation",
            briefDescription = "Field without @SerializedName and @Expose annotation can cause data parsing error.",
            explanation = "Field without @SerializedName and @Expose annotation can cause data parsing error. " +
                "Add @SerializedName and @Expose annotation to avoid error.",
            category = Category.CORRECTNESS,
            priority = 10,
            severity = Severity.FATAL,
            implementation = Implementation(
                SerializedNameAnnotationDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val PARAM_ISSUE = Issue.create(
            id = "ParamFieldAnnotation",
            briefDescription = "Field without @SerializedName and @Expose annotation can cause data parsing error.",
            explanation = "Field without @SerializedName and @Expose annotation can cause data parsing error. " +
                "Add @SerializedName and @Expose annotation to avoid error.",
            category = Category.CORRECTNESS,
            priority = 10,
            severity = Severity.WARNING,
            implementation = Implementation(
                SerializedNameAnnotationDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        private const val RESPONSE_KEYWORD = "Response"
        private const val PARAM_KEYWORD = "Param"
        private const val ENTITY_KEYWORD = "Entity"
        private const val DOMAIN_MODEL_PATH= "domain/model"
        private const val SERIALIZED_NAME_ANNOTATION = "com.google.gson.annotations.SerializedName"
        private const val EXPOSE_ANNOTATION = "com.google.gson.annotations.Expose"
        private const val ENTITY_ANNOTATION = "androidx.room.Entity"
        private const val MESSAGE = "Field \"%s\" without @SerializedName and @Expose annotation can cause data parsing error. " +
            "Add @SerializedName and @Expose annotation to avoid error."
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UParameter::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitParameter(node: UParameter) {
                val serializedNameAnnotation = node.findAnnotation(SERIALIZED_NAME_ANNOTATION)
                val exposeAnnotation = node.findAnnotation(EXPOSE_ANNOTATION)
                val shouldReportIssue = serializedNameAnnotation == null ||
                    exposeAnnotation == null

                if(shouldCheckAnnotation(context, node) && shouldReportIssue) {
                    reportIssue(context, node)
                }
            }
        }
    }

    private fun shouldCheckAnnotation(context: JavaContext, node: UParameter): Boolean {
        val fileName = context.file.name
        val filePath = context.file.path
        val isDataClass = node.getContainingUClass()?.methods?.map { it.name }
            ?.containsAll(listOf("equals", "hashCode")) == true
        val isEntityClass = isEntityClass(context, node)

        return (fileName.contains(RESPONSE_KEYWORD) || fileName.contains(PARAM_KEYWORD) ||
            fileName.contains(ENTITY_KEYWORD) || filePath.contains(DOMAIN_MODEL_PATH)) &&
            node.uastParent is KotlinConstructorUMethod &&
            (isDataClass || isEntityClass)
    }

    private fun reportIssue(context: JavaContext, node: UParameter) {
        val element = node as UElement
        val fieldName = (element.sourcePsi as? KtParameter)?.name.orEmpty()
        val message = String.format(MESSAGE, fieldName)

        when {
            isResponseClass(context, node) -> {
                context.report(
                    RESPONSE_ISSUE,
                    element,
                    context.getNameLocation(element),
                    message
                )
            }
            isParamClass(context, node) -> {
                context.report(
                    PARAM_ISSUE,
                    element,
                    context.getNameLocation(element),
                    message
                )
            }
            isEntityClass(context, node) -> {
                context.report(
                    ENTITY_ISSUE,
                    element,
                    context.getNameLocation(element),
                    message
                )
            }
        }
    }

    private fun isResponseClass(context: JavaContext, node: UParameter): Boolean {
        val fileName = context.file.name
        val isDataClass = node.getContainingUClass()?.methods?.map { it.name }
            ?.containsAll(listOf("equals", "hashCode")) == true
        return fileName.contains(RESPONSE_KEYWORD) && isDataClass
    }

    private fun isParamClass(context: JavaContext, node: UParameter): Boolean {
        val fileName = context.file.name
        val isDataClass = node.getContainingUClass()?.methods?.map { it.name }
            ?.containsAll(listOf("equals", "hashCode")) == true
        return fileName.contains(PARAM_KEYWORD) && isDataClass
    }

    private fun isEntityClass(context: JavaContext, node: UParameter): Boolean {
        val fileName = context.file.name
        val isEntityClass = node.getContainingUClass()?.findAnnotation(ENTITY_ANNOTATION) != null
        return isEntityClass || fileName.contains(ENTITY_KEYWORD)
    }
}