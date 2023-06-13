package com.tokopedia.logisticcart.scheduledelivery.preference

import android.content.Context
import android.content.SharedPreferences

class ScheduleDeliveryPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(SCHEDULE_DELIVERY_PREFERENCE, Context.MODE_PRIVATE)
    }

    var isDisplayedCoachmark: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_DISPLAYED_COACHMARK, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_IS_DISPLAYED_COACHMARK, value).apply()

    companion object {
        private const val SCHEDULE_DELIVERY_PREFERENCE = "schedule_delivery_preference"
        private const val KEY_IS_DISPLAYED_COACHMARK = "key_is_displayed_coachmark"
    }
}
