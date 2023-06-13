package com.tokopedia.linter.detectors.sourcescanner.elements

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.JavaContext
import com.tokopedia.linter.detectors.sourcescanner.elements.annotation.AnnotationDetector
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UImportStatement
import org.jetbrains.uast.USimpleNameReferenceExpression

class ElementDetector(private val context: JavaContext) : UElementHandler() {
    override fun visitImportStatement(node: UImportStatement) {
//        WrongImportDetector
        ImportDetector.checkImport(context, node)
    }

    override fun visitAnnotation (node: UAnnotation) {
        AnnotationDetector.checkAnnotation(context,node)
    }

    override fun visitSimpleNameReferenceExpression(node: USimpleNameReferenceExpression) {
        ElementNameDetector.checkElementName(context, node)
    }
}
