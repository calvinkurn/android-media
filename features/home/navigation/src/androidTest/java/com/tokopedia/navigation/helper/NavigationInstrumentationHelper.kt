package com.tokopedia.navigation.com.tokopedia.navigation.helper

import android.content.Context
import com.tokopedia.home.beranda.presentation.view.helper.*

object NavigationInstrumentationHelper {
    private const val KEY_FIRST_VIEW_NAVIGATION = "KEY_FIRST_VIEW_NAVIGATION"
    private const val KEY_FIRST_VIEW_NAVIGATION_ONBOARDING = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING"
    private const val CHOOSE_ADDRESS_PREFERENCE_NAME = "coahmark_choose_address"
    private const val CHOOSE_ADDRESS_EXTRA_IS_COACHMARK = "EXTRA_IS_COACHMARK"

    fun disableCoachMark(context: Context){
        disableOnboarding(context)
        disableChooseAddressCoachmark(context)
        setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_COACHMARK, true)
        setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_COACHMARK_NAV, true)
        setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_COACHMARK_INBOX, true)
        setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_COACHMARK_CHOOSEADDRESS, true)
        setCoachmarkSharedPrefValue(context, PREF_KEY_HOME_COACHMARK_BALANCE, true)
    }

    fun setCoachmarkSharedPrefValue(context: Context, key: String, value: Boolean) {
        val sharedPrefs = context.getSharedPreferences(PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(key, value).apply()
    }

    fun disableOnboarding(context: Context) {
        val sharedPrefs = context.getSharedPreferences(KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(
                KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false).apply()
    }

    fun disableChooseAddressCoachmark(context: Context) {
        val sharedPrefs = context.getSharedPreferences(CHOOSE_ADDRESS_PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(
                CHOOSE_ADDRESS_EXTRA_IS_COACHMARK, false).apply()
    }
}