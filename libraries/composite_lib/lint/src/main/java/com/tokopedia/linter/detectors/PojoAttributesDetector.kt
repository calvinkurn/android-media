package com.tokopedia.linter.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.asJava.elements.KtLightField
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UField
import org.jetbrains.uast.kotlin.KotlinUClass
import org.jetbrains.uast.kotlin.KotlinUField


class PojoAttributesDetector: Detector(), Detector.UastScanner {

    override fun getApplicableUastTypes(): List<java.lang.Class<out UElement>> {
        return listOf(UClass::class.java)
    }

    override fun createUastHandler(context: JavaContext): DataClassVisitor = DataClassVisitor(context)

    companion object {
        private const val ERROR_MESSAGE = "Attributes in response or payload POJO should be annotated with @SerializedName"

        val JAVA_ISSUE = Issue.create(
            id = "PojoAttributes",
            briefDescription = ERROR_MESSAGE,
            explanation = "@SerializedName annotation will prevent the attribute for being obfuscated by proguard",
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

    class DataClassVisitor(private val context: JavaContext): UElementHandler() {
        override fun visitClass(node: UClass) {
            if (node !is KotlinUClass) return
            val isDataClass = node.methods
                .map { it.name }
                .containsAll(listOf("equals", "hashCode"))
            if (isDataClass) {
                val fields = node
                    .allFields
                    .filterIsInstance<KtLightField>()
                checkFields(fields)
            }
        }

        private fun checkFields(fields: List<KtLightField>) {
            fields.forEach { field ->
                if (field.getAnnotation(SERIALIZED_NAME) == null) {
                    report(field)
                }
            }
        }

        private fun report(node: PsiElement) {
            context.report(
                issue = JAVA_ISSUE,
                scope = node,
                location = context.getNameLocation(node),
                message = ERROR_MESSAGE
            )
        }
    }

}