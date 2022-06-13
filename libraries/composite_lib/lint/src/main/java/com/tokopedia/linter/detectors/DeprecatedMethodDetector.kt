package com.tokopedia.linter.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.AnnotationUsageType
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.google.common.collect.Sets
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement

class DeprecatedMethodDetector: Detector(), SourceCodeScanner {

    companion object {
        val ISSUE = Issue.create(
            id = "DeprecatedMethod",
            briefDescription = "Deprecated method should not be used.",
            explanation = "Method is deprecated, please use its replacement instead.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                DeprecatedMethodDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        private const val JAVA_DEPRECATED_ANNOTATION = "java.lang.Deprecated"
        private const val KOTLIN_DEPRECATED_ANNOTATION = "kotlin.Deprecated"
        private const val ERROR_MESSAGE = "Method is deprecated, please use its replacement instead."
    }

    private val deprecatedMethods = Sets.newHashSet<String>()

    override fun afterCheckEachProject(context: Context) {
        if (context.phase == 1) {
            context.driver.requestRepeat(this, Scope.JAVA_FILE_SCOPE)
        }
    }

    override fun applicableAnnotations(): List<String> {
        return listOf(JAVA_DEPRECATED_ANNOTATION, KOTLIN_DEPRECATED_ANNOTATION)
    }

    override fun visitAnnotationUsage(
        context: JavaContext,
        usage: UElement,
        type: AnnotationUsageType,
        annotation: UAnnotation,
        qualifiedName: String,
        method: PsiMethod?,
        annotations: List<UAnnotation>,
        allMemberAnnotations: List<UAnnotation>,
        allClassAnnotations: List<UAnnotation>,
        allPackageAnnotations: List<UAnnotation>
    ) {
        if(context.phase == 1) {
            method?.run {
                val packageName = getPackageName(containingFile)
                val className = containingClass?.name.orEmpty()
                val deprecatedMethod = "$packageName.$className.$name"
                deprecatedMethods.add(deprecatedMethod)
            }
        }
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UCallExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) {
                if(context.phase == 2) {
                    scanMethod(context, node)
                }
            }
        }
    }

    private fun scanMethod(context: JavaContext, node: UCallExpression) {
        deprecatedMethods.firstOrNull {
            it.substringAfterLast(".") == node.methodName
        }?.let {
            val evaluator = context.evaluator
            val packageName = it.substringBeforeLast(".")
            if(evaluator.isMemberInClass(node.resolve(), packageName)) {
                reportJavaError(context, node)
            }
        }
    }

    private fun reportJavaError(context: JavaContext, node: UCallExpression) {
        context.report(
            ISSUE,
            node,
            context.getNameLocation(node),
            ERROR_MESSAGE
        )
    }

    private fun getPackageName(file: PsiFile): String {
        return when(val psiFile = file.containingFile) {
            is PsiJavaFile -> psiFile.packageName
            is KtFile -> psiFile.packageFqName.asString()
            else -> ""
        }
    }
}