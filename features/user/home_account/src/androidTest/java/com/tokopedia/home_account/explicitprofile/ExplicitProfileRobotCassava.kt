package com.tokopedia.home_account.explicitprofile

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess


infix fun ExplicitProfileResult.validateTracker(action: ExplicitProfileRobotCassava.() -> Unit): ExplicitProfileRobotCassava {
    Thread.sleep(1000)
    return ExplicitProfileRobotCassava().apply(action)
}

class ExplicitProfileRobotCassava {

    private fun generateTrackerQuery(
        action: String,
        label: String,
    ): Map<String, String> {
        return mapOf(
            "event" to "clickExplicitProfile",
            "eventCategory" to "explicit form page",
            "eventAction" to action,
            "eventLabel" to label,
            "businessUnit" to "User Platform",
            "currentSite" to "tokopediamarketplace"
        )
    }

    private fun validateTracker(query: Map<String, String>, cassavaTestRule: CassavaTestRule) {
        val queryMatcher = cassavaTestRule.validate(
            listOf(query),
            CassavaTestRule.MODE_SUBSET
        )

        assertThat(queryMatcher, hasAllSuccess())
    }

    fun CassavaTestRule.validateTrackerOnClickTab(tabName: String) {
        val query = generateTrackerQuery(
            action = "click category tabs",
            label = tabName
        )

        validateTracker(query, this)
    }

    fun CassavaTestRule.validateTrackerOnClickSectionInfo() {
        val query = generateTrackerQuery(
            action = "click information icon",
            label = ""
        )

        validateTracker(query, this)
    }

    fun CassavaTestRule.validateTrackerOnClickAnswer(selectedAnswers: String) {
        val query = generateTrackerQuery(
            action = "click option value",
            label = selectedAnswers
        )

        validateTracker(query, this)
    }

    fun CassavaTestRule.validateTrackerOnSavePreference(isSuccess: Boolean, message: String = "") {
        val query = generateTrackerQuery(
            action = "click simpan",
            label = if (isSuccess) "success" else "fail - $message"
        )

        validateTracker(query, this)
    }
}