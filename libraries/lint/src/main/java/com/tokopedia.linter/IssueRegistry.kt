@file:Suppress("UnstableApiUsage")

package com.tokopedia.linter

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

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
