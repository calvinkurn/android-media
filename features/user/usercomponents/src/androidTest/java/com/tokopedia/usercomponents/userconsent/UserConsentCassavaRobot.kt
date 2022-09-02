package com.tokopedia.usercomponents.userconsent

infix fun UserConsentResult.validateTracker(action: UserConsentCassavaRobot.() -> Unit): UserConsentCassavaRobot {
    Thread.sleep(3000)
    return UserConsentCassavaRobot().apply(action)
}

class UserConsentCassavaRobot {

    companion object {
        const val QUERY_TNC_SINGLE_MANDATORY_PURPOSE = "247"
        const val QUERY_TNC_POLICY_SINGLE_MANDATORY_PURPOSE = "248"
        const val QUERY_TNC_SINGLE_OPTIONAL_PURPOSE = "249"
        const val QUERY_TNC_POLICY_SINGLE_OPTIONAL_PURPOSE = "250"
        const val QUERY_TNC_MULTIPLE_OPTIONAL_PURPOSE = "251"
        const val QUERY_TNC_POLICY_MULTIPLE_OPTIONAL_PURPOSE = "252"
    }
}