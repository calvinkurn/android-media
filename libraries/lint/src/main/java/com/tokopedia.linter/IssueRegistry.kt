
package com.tokopedia.linter

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.tokopedia.linter.detectors.CoreResourcesDetector
import com.tokopedia.linter.detectors.SetColourDetector
import com.tokopedia.linter.detectors.TypographyDetector

class IssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(
                SetColourDetector.ISSUE,
                CoreResourcesDetector.ISSUE,
                TypographyDetector.ISSUE
        )

    override val minApi: Int
        get() = 1

    override val api: Int = CURRENT_API
}
