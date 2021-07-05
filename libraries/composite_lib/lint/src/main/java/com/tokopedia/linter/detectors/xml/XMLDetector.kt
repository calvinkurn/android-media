package com.tokopedia.linter.detectors.xml

import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.unify.UnifyDetector
import org.w3c.dom.Element

class XMLDetector : Detector(), XmlScanner {
    companion object {
        internal val IMPLEMENTATION = Implementation(
                XMLDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
        )
    }
    override fun getApplicableElements(): Collection<String>? {
        return UnifyDetector.unifyMapKeys.keys
    }

    override fun visitElement(context: XmlContext, element: Element) {
        UnifyDetector.checkUnify(context, element)
    }
}