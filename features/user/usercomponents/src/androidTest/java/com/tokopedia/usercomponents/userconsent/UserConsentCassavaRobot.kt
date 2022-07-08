package com.tokopedia.usercomponents.userconsent

import com.tokopedia.cassavatest.CassavaTestRule

infix fun UserConsentResult.validateTracker(action: CassavaTestRule.() -> Unit): CassavaTestRule {
    Thread.sleep(100)
    return CassavaTestRule().apply(action)
}

object UserConsentCassavaRobot {
    const val QUERY_TNC_SINGLE_MANDATORY_PURPOSE = "247"
    const val QUERY_TNC_POLICY_SINGLE_MANDATORY_PURPOSE = "248"
    const val QUERY_TNC_SINGLE_OPTIONAL_PURPOSE = "249"
    const val QUERY_TNC_POLICY_SINGLE_OPTIONAL_PURPOSE = "250"
    const val QUERY_TNC_MULTIPLE_OPTIONAL_PURPOSE = "251"
    const val QUERY_TNC_POLICY_MULTIPLE_OPTIONAL_PURPOSE = "252"
}