package com.tokopedia.home.beranda.presentation.view.helper

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils

/**
 * Created by yfsx on 3/10/21.
 */

const val PREF_KEY_HOME_COACHMARK = "PREF_KEY_HOME_COACHMARK"
const val PREF_KEY_HOME_COACHMARK_NAV = "PREF_KEY_HOME_COACHMARK_NAV"
const val PREF_KEY_HOME_COACHMARK_INBOX = "PREF_KEY_HOME_COACHMARK_INBOX"
const val PREF_KEY_HOME_COACHMARK_CHOOSEADDRESS = "PREF_KEY_HOME_COACHMARK_CHOOSEADDRESS"
const val PREF_KEY_HOME_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_BALANCE"


fun isNavigationCoachmarkShown(context: Context): Boolean {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    return sharedPrefs.getBoolean(PREF_KEY_HOME_COACHMARK_NAV, false)
}

fun setNavigationCoachmarkShown(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(PREF_KEY_HOME_COACHMARK_NAV, true).apply()
}

fun isInboxCoachmarkShown(context: Context): Boolean {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    return sharedPrefs.getBoolean(PREF_KEY_HOME_COACHMARK_INBOX, false)
}

fun setInboxCoachmarkShown(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(PREF_KEY_HOME_COACHMARK_INBOX, true).apply()
}

fun isLocalizingAddressNeedShowCoachMark(context: Context): Boolean {
    return  ChooseAddressUtils.isLocalizingAddressNeedShowCoachMark(context) ?: false
}

fun setChooseAddressCoachmarkShown(context: Context) {
    ChooseAddressUtils.coachMarkLocalizingAddressAlreadyShown(context)
}

fun isBalanceWidgetCoachmarkShown(context: Context): Boolean {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    return sharedPrefs.getBoolean(PREF_KEY_HOME_COACHMARK_BALANCE, false)
}

fun setBalanceWidgetCoachmarkShown(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(PREF_KEY_HOME_COACHMARK_BALANCE, true).apply()
}