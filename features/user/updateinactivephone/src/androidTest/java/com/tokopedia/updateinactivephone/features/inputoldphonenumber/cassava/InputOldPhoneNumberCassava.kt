package com.tokopedia.updateinactivephone.features.inputoldphonenumber.cassava

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberTest

object InputOldPhoneNumberCassava {

    private const val KEY_EVENT = "event"
    private const val KEY_EVENT_CATEGORY = "eventCategory"
    private const val KEY_EVENT_ACTION = "eventAction"
    private const val KEY_EVENT_LABEL = "eventLabel"
    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_CURRENT_SITE = "currentSite"
    private const val KEY_TRACKER_ID = "trackerId"
    private const val VALUE_EVENT = "clickAccount"
    private const val VALUE_EVENT_CATEGORY = "account setting - change phone number"
    private const val VALUE_EVENT_ACTION = "click on button lanjut"
    private const val VALUE_BUSINESS_UNIT = "user platform"
    private const val VALUE_CURRENT_SITE = "tokopediamarketplace"
    private const val VALUE_TRACKER_ID_30678 = "30678"
    private const val INACTIVE_PHONE_NUMBER = "inactive phone number"
    private const val CLICK = "click"
    private const val FAILED = "failed"
    private const val SUCCESS = "success"

    private fun generateTrackerQuery(
        label: String,
    ): Map<String, String> {
        val mapTracker = mapOf(
            KEY_EVENT to VALUE_EVENT,
            KEY_EVENT_CATEGORY to VALUE_EVENT_CATEGORY,
            KEY_EVENT_ACTION to VALUE_EVENT_ACTION,
            KEY_EVENT_LABEL to "$label - $INACTIVE_PHONE_NUMBER",
            KEY_BUSINESS_UNIT to VALUE_BUSINESS_UNIT,
            KEY_CURRENT_SITE to VALUE_CURRENT_SITE
        )
        mapTracker[KEY_TRACKER_ID] to VALUE_TRACKER_ID_30678

        return mapTracker
    }

    fun CassavaTestRule.validateTrackerOnSubmitPhone(
        state: InputOldPhoneNumberCassavaState
    ) {
        val query = when (state) {
            InputOldPhoneNumberCassavaState.CLICK -> generateTrackerQuery(CLICK)
            InputOldPhoneNumberCassavaState.EMPTY_PHONE -> generateTrackerQuery("$FAILED - ${InputOldPhoneNumberTest.ERROR_PHONE_EMPTY}")
            InputOldPhoneNumberCassavaState.TOO_SHORT_PHONE -> generateTrackerQuery("$FAILED - ${InputOldPhoneNumberTest.ERROR_PHONE_TOO_SHORT}")
            InputOldPhoneNumberCassavaState.TOO_LONG_PHONE -> generateTrackerQuery("$FAILED - ${InputOldPhoneNumberTest.ERROR_PHONE_TOO_LONG}")
            InputOldPhoneNumberCassavaState.REGISTERED_PHONE -> generateTrackerQuery(SUCCESS)
            InputOldPhoneNumberCassavaState.UNREGISTERED_PHONE -> generateTrackerQuery("$FAILED - ${InputOldPhoneNumberTest.ERROR_PHONE_NOT_REGISTERED}")
        }

        val queryMatcher = this.validate(
            listOf(query),
            CassavaTestRule.MODE_SUBSET
        )

        ViewMatchers.assertThat(queryMatcher, hasAllSuccess())
    }
}