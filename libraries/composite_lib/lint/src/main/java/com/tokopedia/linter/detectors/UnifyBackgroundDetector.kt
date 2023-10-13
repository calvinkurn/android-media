package com.tokopedia.linter.detectors

import com.android.AndroidXConstants.CONSTRAINT_LAYOUT
import com.android.SdkConstants.ATTR_BACKGROUND
import com.android.SdkConstants.FRAME_LAYOUT
import com.android.SdkConstants.LINEAR_LAYOUT
import com.android.SdkConstants.RELATIVE_LAYOUT
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

class UnifyBackgroundDetector : Detector(), XmlScanner {

    companion object {
        val ISSUE = Issue.create(
            id = "UnifyBackground",
            briefDescription = "Use Unify_Background instead of Unify_N0.",
            explanation = "Please use Unify_Background instead of Unify_N0 for background layout.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                UnifyBackgroundDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )

        private const val UNIFY_N0 = "@color/Unify_N0"
        private const val UNIFY_BACKGROUND = "@color/Unify_Background"
        private const val ERROR_MESSAGE = "Please use Unify_Background instead of Unify_N0 for background layout."
    }

    private val layoutNames = arrayListOf(
        FRAME_LAYOUT,
        LINEAR_LAYOUT,
        RELATIVE_LAYOUT,
        CONSTRAINT_LAYOUT.oldName(),
        CONSTRAINT_LAYOUT.newName()
    )

    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return folderType == ResourceFolderType.LAYOUT
    }

    override fun getApplicableAttributes(): Collection<String>? {
        return listOf(ATTR_BACKGROUND)
    }

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        val ownerName = attribute.ownerElement.nodeName
        if (layoutNames.contains(ownerName)) {
            scanBackgroundAttr(context, attribute)
        }
    }

    private fun scanBackgroundAttr(context: XmlContext, attribute: Attr) {
        if (attribute.value == UNIFY_N0) {
            reportIssue(context, attribute)
        }
    }

    private fun reportIssue(context: XmlContext, attr: Attr) {
        val lintFix = LintFix.create()
            .replace()
            .text(attr.value)
            .with(UNIFY_BACKGROUND)
            .build()

        context.report(
            ISSUE,
            attr,
            context.getValueLocation(attr),
            ERROR_MESSAGE,
            lintFix
        )
    }
}
