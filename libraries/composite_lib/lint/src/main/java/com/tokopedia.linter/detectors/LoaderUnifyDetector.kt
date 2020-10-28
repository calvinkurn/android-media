package com.tokopedia.linter.detectors

import com.android.SdkConstants
import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.*
import org.w3c.dom.Element


class LoaderUnifyDetector : Detector(), XmlScanner {

    companion object {
        private val IMPLEMENTATION = Implementation(
                LoaderUnifyDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
        )

        val ISSUE: Issue = Issue.create(
                PROGRESSBAR_ISSUE_ID,
                WARNING_DESCRIPTION_FORMAT.format(PROGRESSBAR_OLD_NAME, PROGRESSBAR_NEW_NAME),
                WARNING_EXPLANATION_FORMAT.format(PROGRESSBAR_OLD_NAME, PROGRESSBAR_NEW_NAME),
                Category.CORRECTNESS,
                3,
                Severity.WARNING,
                IMPLEMENTATION)
    }

    override fun getApplicableElements(): Collection<String>? {
        return listOf(SdkConstants.PROGRESS_BAR)
    }

    override fun visitElement(context: XmlContext, element: Element) {
        val loaderUnify = LintFix.create().name(FIX_NAME_FORMAT.format(PROGRESSBAR_NEW_NAME_SHORT))
                .replace()
                .text(PROGRESSBAR_OLD_NAME)
                .with(PROGRESSBAR_NEW_NAME)
                .robot(true)
                .independent(true)
                .build()


        context.report(ISSUE, location = context.getElementLocation(element),
                message = USAGE_ERROR_MESSAGE_FORMAT.format(PROGRESSBAR_NEW_NAME_SHORT), quickfixData = loaderUnify)
    }
}
