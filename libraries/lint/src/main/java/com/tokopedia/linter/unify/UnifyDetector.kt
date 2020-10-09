package com.tokopedia.linter.unify

import com.android.SdkConstants
import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.FLOATING_BUTTON_ISSUE_ID
import com.tokopedia.linter.detectors.xml.XMLDetector

class UnifyDetector {
    companion object {

        val unifyMap = mapOf(SdkConstants.FLOATING_ACTION_BUTTON to "")

        private val IMPLEMENTATION = Implementation(
                XMLDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
        )

        val ISSUE: Issue = Issue.create(
                FLOATING_BUTTON_ISSUE_ID,
                "Widget replaced with UnifyComponent",
                "We have Unify Components Tokopedia Recomment to use them",
                Category.CORRECTNESS,
                3,
                Severity.WARNING,
                IMPLEMENTATION)
    }

}