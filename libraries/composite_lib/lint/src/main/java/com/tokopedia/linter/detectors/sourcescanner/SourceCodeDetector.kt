package com.tokopedia.linter.detectors.sourcescanner

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.intellij.psi.PsiMethod
import com.tokopedia.linter.detectors.sourcescanner.constructor.ConstructorDetector
import com.tokopedia.linter.detectors.sourcescanner.constructor.ConstructorDetector.applicableConstructorList
import com.tokopedia.linter.detectors.sourcescanner.elements.ElementDetector
import com.tokopedia.linter.detectors.sourcescanner.method.MethodCallDetector
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UImportStatement

@Suppress("UnstableApiUsage")
class SourceCodeDetector : Detector(), Detector.UastScanner {
    companion object {
        internal val IMPLEMENTATION = Implementation(
                SourceCodeDetector::class.java,
                Scope.JAVA_FILE_SCOPE
        )
    }


    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        return listOf(UImportStatement::class.java)
    }


    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return ElementDetector(context)
    }

    override fun getApplicableConstructorTypes(): List<String>? {
        return applicableConstructorList.toList()
    }

    override fun visitConstructor(
            context: JavaContext,
            node: UCallExpression,
            constructor: PsiMethod
    ) {
        ConstructorDetector.checkConstructor(context, node, constructor)
    }

    override fun getApplicableMethodNames(): List<String>? = MethodCallDetector.applicableMethodNames

    override fun visitMethodCall(
            context: JavaContext,
            node: UCallExpression,
            method: PsiMethod
    ) {
       MethodCallDetector.checkMethod(context,node, method)
    }
}
