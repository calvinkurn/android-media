package com.tokopedia.loginregister.common

import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import org.hamcrest.MatcherAssert

object CassavaTestRuleMatcher {
    fun validate(cassavaTestRule: CassavaTestRule, query: List<Map<String, String>>) {
        MatcherAssert.assertThat(
            cassavaTestRule.validate(query, CassavaTestRule.MODE_SUBSET),
            hasAllSuccess()
        )
    }

    fun validateExact(cassavaTestRule: CassavaTestRule, query: List<Map<String, String>>) {
        MatcherAssert.assertThat(
            cassavaTestRule.validate(query, CassavaTestRule.MODE_EXACT),
            hasAllSuccess()
        )
    }

    fun getAnalyticValidator(
        event: String, category: String, action: String, label: String
    ): Map<String, String> {
        return mapOf(
            Event.EVENT_KEY to event,
            Event.CATEGORY_KEY to category,
            Event.ACTION_KEY to action,
            Event.LABEL_KEY to label
        )
    }
}