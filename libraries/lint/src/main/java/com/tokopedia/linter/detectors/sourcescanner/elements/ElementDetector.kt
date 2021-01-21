package com.tokopedia.linter.detectors.sourcescanner.elements

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.JavaContext
import org.jetbrains.uast.UImportStatement

class ElementDetector(private val context: JavaContext) : UElementHandler() {
    override fun visitImportStatement(node: UImportStatement) {
//        WrongImportDetector
        ImportDetector.checkImport(context, node)
    }
}