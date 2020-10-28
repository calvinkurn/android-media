package com.tokopedia.linter.detectors

import com.android.SdkConstants
import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.*
import org.w3c.dom.Element

class SearchBarUnifyDetector : Detector(), XmlScanner {

    companion object {
        private val IMPLEMENTATION = Implementation(
                SearchBarUnifyDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
        )

        val ISSUE: Issue = Issue.create(
                SEARCHBAR_ISSUE_ID,
                WARNING_DESCRIPTION_FORMAT.format(SEARCHBAR_OLD_NAME, SEARCHBAR_NEW_NAME),
                WARNING_EXPLANATION_FORMAT.format(SEARCHBAR_OLD_NAME, SEARCHBAR_NEW_NAME),
                Category.CORRECTNESS,
                3,
                Severity.WARNING,
                IMPLEMENTATION)
    }


    override fun getApplicableElements(): Collection<String>? {
        return listOf(SdkConstants.SEARCH_VIEW)
    }

    override fun visitElement(context: XmlContext, element: Element) {
        val searchBarUnify = LintFix.create().name(FIX_NAME_FORMAT.format(SEARCHBAR_NEW_NAME))
                .replace()
                .text(SEARCHBAR_OLD_NAME)
                .with(SEARCHBAR_NEW_NAME)
                .robot(true)
                .independent(true)
                .build()


        context.report(ISSUE, location = context.getElementLocation(element),
                message = USAGE_ERROR_MESSAGE_FORMAT.format(SEARCHBAR_NEW_NAME), quickfixData = searchBarUnify)

        val searchBarViewUnify = LintFix.create().name(FIX_NAME_FORMAT.format(SEARCHBAR_NEW_NAME))
                .replace()
                .text(SEARCHBARVIEW_OLD_NAME)
                .with(SEARCHBAR_NEW_NAME)
                .robot(true)
                .independent(true)
                .build()


        context.report(ISSUE, location = context.getElementLocation(element),
                message = USAGE_ERROR_MESSAGE_FORMAT.format(SEARCHBAR_NEW_NAME), quickfixData = searchBarViewUnify)
    }

}
