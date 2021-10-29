package com.tokopedia.linter.detectors

import com.android.SdkConstants
import com.android.resources.ResourceFolderType
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.XmlContext
import com.android.tools.lint.detector.api.XmlScanner
import org.w3c.dom.Attr
import org.w3c.dom.Element

class DimenResourceNameDetector : Detector(), XmlScanner {
    companion object {
        val ISSUE = Issue.create(
            id = "DimenResourceName",
            briefDescription = "Remove dimen in resource name to increase build performance.",
            explanation = "Resource name without dimen prefix have better build performance.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                DimenResourceNameDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )

        private const val RESOURCE_NAME_DIMEN = "dimen"
        private const val ERROR_MESSAGE = "Resource name without dimen prefix have better build performance."
    }

    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return folderType == ResourceFolderType.VALUES
    }

    override fun getApplicableElements(): Collection<String> {
        return listOf(SdkConstants.TAG_DIMEN)
    }

    override fun visitElement(context: XmlContext, element: Element) {
        val attribute = element.getAttributeNode("name")
        if (attribute.value.contains(RESOURCE_NAME_DIMEN)) {
            reportError(context, element, attribute)
        }
    }

    private fun reportError(context: XmlContext, node: Element, attribute: Attr) {
        val attrValue = attribute.value
        val suggestion = if(attrValue.startsWith("${RESOURCE_NAME_DIMEN}_")) {
            attrValue.replace("${RESOURCE_NAME_DIMEN}_", "")
        } else {
            attrValue.replace("_${RESOURCE_NAME_DIMEN}", "")
        }

        val quickFix = LintFix.create()
            .replace()
            .text(attrValue)
            .with(suggestion)
            .build()

        context.report(
            ISSUE,
            node,
            context.getValueLocation(attribute),
            ERROR_MESSAGE,
            quickFix
        )
    }
}