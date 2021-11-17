package com.tokopedia.linter.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.asJava.elements.KtLightField
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.kotlin.KotlinUClass


class PojoAttributesDetector: Detector(), Detector.UastScanner {

    companion object {
        private const val ERROR_MESSAGE = "Attributes in response or payload POJO should be annotated with @SerializedName"

        val JAVA_ISSUE = Issue.create(
            id = "PojoAttributes",
            briefDescription = ERROR_MESSAGE,
            explanation = "@SerializedName annotation will prevent the attribute for being obfuscated by proguard" +
                "Please add the @SerializedName annotation on this field/attribute. " ,
            category = Category.CORRECTNESS,
            priority = 10,
            severity = Severity.WARNING,
            implementation = Implementation(
                PojoAttributesDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        private const val SERIALIZED_NAME = "com.google.gson.annotations.SerializedName"
    }

    override fun getApplicableUastTypes(): List<java.lang.Class<out UElement>> {
        return listOf(UClass::class.java)
    }

    override fun createUastHandler(context: JavaContext): DataClassVisitor = DataClassVisitor(context)

    class DataClassVisitor(private val context: JavaContext): UElementHandler() {

        private val parentDirectoryList = listOf("/domain", "/data")
        private val classNameList = listOf("Response", "Param", "Data")

        override fun visitClass(node: UClass) {
            if (node !is KotlinUClass) return
            val isDataClass = node.methods
                .map { it.name }
                .containsAll(listOf("equals", "hashCode"))
            if (isDataClass && isResponsePojo(node)) {
                val fields = node
                    .allFields
                    .filterIsInstance<KtLightField>()
                checkFields(fields, ERROR_MESSAGE)
            }
        }

        private fun isResponsePojo(node: UClass): Boolean {
            val containingDir = node.context?.getContainingFile()?.containingDirectory?.toString()
            val fileName = node.context?.containingFile?.originalFile?.toString()

            val isInsideDesignatedDirectory = containingDir?.let { dir ->
                checkForDirectory(dir)
            } ?: false
            val isFileNameContainsDesignatedKeyword = fileName?.let { name ->
                checkForClassName(name)
            } ?: false
            return isInsideDesignatedDirectory && isFileNameContainsDesignatedKeyword
        }

        private fun checkForDirectory(containingDir: String): Boolean {
            parentDirectoryList.forEach { dir ->
                if (containingDir.contains(dir)) {
                    return true
                }
            }
            return false
        }

        private fun checkForClassName(className: String): Boolean {
            classNameList.forEach { name ->
                if (className.contains(name)) {
                    return true
                }
            }
            return false
        }

        private fun checkFields(fields: List<KtLightField>, message: String) {
            fields.forEach { field ->
                if (field.getAnnotation(SERIALIZED_NAME) == null) {
                    report(field, message)
                }
            }
        }

        private fun report(node: PsiElement, message: String) {
            context.report(
                issue = JAVA_ISSUE,
                scope = node,
                location = context.getNameLocation(node),
                message = message
            )
        }
    }

}