package com.tokopedia.home.beranda.presentation.view.helper

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils

/**
 * Created by yfsx on 3/10/21.
 */

const val PREF_KEY_HOME_COACHMARK = "PREF_KEY_HOME_COACHMARK"
const val PREF_KEY_HOME_COACHMARK_INBOX = "PREF_KEY_HOME_COACHMARK_INBOX"
const val PREF_KEY_HOME_COACHMARK_CHOOSEADDRESS = "PREF_KEY_HOME_COACHMARK_CHOOSEADDRESS"
const val PREF_KEY_HOME_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_BALANCE"
const val PREF_KEY_WALLETAPP_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_WALLETAPP"
const val PREF_KEY_WALLETAPP2_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_WALLETAPP2"
const val PREF_KEY_NEW_WALLETAPP_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_NEW_WALLETAPP"
const val PREF_KEY_NEW_TOKOPOINT_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_NEW_TOKOPOINT"
const val PREF_KEY_HOME_TOKONOW_COACHMARK = "PREF_KEY_HOME_TOKONOW_COACHMARK"
const val PREF_KEY_SUBSCRIPTION_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_SUBSCRIPTION"

fun setSubscriptionCoachmarkShown(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(PREF_KEY_SUBSCRIPTION_COACHMARK_BALANCE, true).apply()
}

fun setHomeTokonowCoachmarkShown(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_HOME_TOKONOW_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(PREF_KEY_HOME_TOKONOW_COACHMARK, true).apply()
}

fun isSubscriptionCoachmarkShown(context: Context): Boolean {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    return sharedPrefs.getBoolean(PREF_KEY_SUBSCRIPTION_COACHMARK_BALANCE, false)
}

fun isHomeTokonowCoachmarkShown(context: Context): Boolean {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_HOME_TOKONOW_COACHMARK, Context.MODE_PRIVATE)
    return sharedPrefs.getBoolean(PREF_KEY_HOME_TOKONOW_COACHMARK, false)
}

