package com.tokopedia.updateinactivephone.features.inputoldphonenumber

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess

object InputOldPhoneNumberCassava {

    private fun generateTrackerQuery(
        label: String,
    ): Map<String, String> {
        return mapOf(
            "event" to "clickAccount",
            "eventCategory" to "account setting - change phone number",
            "eventAction" to "click on button lanjut",
            "eventLabel" to label,
            "businessUnit" to "user platform",
            "currentSite" to "tokopediamarketplace"
        )
    }

    fun CassavaTestRule.validateTrackerOnSubmitPhone(
        state: InputOldPhoneNumberCassavaState,
        phoneNumber: String = ""
    ) {
        val query = when (state) {
            InputOldPhoneNumberCassavaState.CLICK -> generateTrackerQuery("click")
            InputOldPhoneNumberCassavaState.EMPTY_PHONE -> generateTrackerQuery("failed - ${InputOldPhoneNumberGeneralTest.ERROR_PHONE_EMPTY} - $phoneNumber")
            InputOldPhoneNumberCassavaState.TOO_SHORT_PHONE -> generateTrackerQuery("failed - ${InputOldPhoneNumberGeneralTest.ERROR_PHONE_TOO_SHORT} - $phoneNumber")
            InputOldPhoneNumberCassavaState.TOO_LONG_PHONE -> generateTrackerQuery("failed - ${InputOldPhoneNumberGeneralTest.ERROR_PHONE_TOO_LONG} - $phoneNumber")
            InputOldPhoneNumberCassavaState.REGISTERED_PHONE -> generateTrackerQuery("success")
            InputOldPhoneNumberCassavaState.UNREGISTERED_PHONE -> generateTrackerQuery("failed - ${InputOldPhoneNumberNotRegisteredTest.ERROR_PHONE_NOT_REGISTERED} - $phoneNumber")
        }

        val queryMatcher = this.validate(
            listOf(query),
            CassavaTestRule.MODE_SUBSET
        )

        ViewMatchers.assertThat(queryMatcher, hasAllSuccess())
    }
}