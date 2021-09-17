package com.tokopedia.home.beranda.presentation.view.helper

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by yfsx on 3/10/21.
 */

const val PREF_KEY_HOME_COACHMARK = "PREF_KEY_HOME_COACHMARK"
const val PREF_KEY_HOME_COACHMARK_NAV = "PREF_KEY_HOME_COACHMARK_NAV"
const val PREF_KEY_HOME_COACHMARK_INBOX = "PREF_KEY_HOME_COACHMARK_INBOX"
const val PREF_KEY_HOME_COACHMARK_CHOOSEADDRESS = "PREF_KEY_HOME_COACHMARK_CHOOSEADDRESS"
const val PREF_KEY_HOME_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_BALANCE"
const val PREF_KEY_WALLETAPP_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_WALLETAPP"
const val PREF_KEY_WALLETAPP2_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_WALLETAPP2"
const val PREF_KEY_NEW_WALLETAPP_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_NEW_WALLETAPP"
const val PREF_KEY_NEW_TOKOPOINT_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_NEW_TOKOPOINT"

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

fun setChooseAddressCoachmarkShown(context: Context) {
    ChooseAddressUtils.coachMarkLocalizingAddressAlreadyShown(context)
}

fun isBalanceWidgetCoachmarkShown(context: Context): Boolean {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    return sharedPrefs.getBoolean(PREF_KEY_HOME_COACHMARK_BALANCE, false)
}

fun isWalletAppCoachmarkShown(context: Context): Boolean {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    return sharedPrefs.getBoolean(PREF_KEY_WALLETAPP_COACHMARK_BALANCE, false)
}

fun isWalletApp2CoachmarkShown(context: Context): Boolean {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    return sharedPrefs.getBoolean(PREF_KEY_WALLETAPP2_COACHMARK_BALANCE, false)
}

fun setBalanceWidgetCoachmarkShown(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(PREF_KEY_HOME_COACHMARK_BALANCE, true).apply()
}

fun setWalletAppCoachmarkShown(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(PREF_KEY_WALLETAPP_COACHMARK_BALANCE, true).apply()
}

fun setWalletApp2CoachmarkShown(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(PREF_KEY_WALLETAPP2_COACHMARK_BALANCE, true).apply()
}

fun setNewWalletAppCoachmarkShown(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(PREF_KEY_NEW_WALLETAPP_COACHMARK_BALANCE, true).apply()
}

fun setNewTokopointCoachmarkShown(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(PREF_KEY_NEW_TOKOPOINT_COACHMARK_BALANCE, true).apply()
}

fun isNewWalletAppCoachmarkShown(context: Context): Boolean {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    return sharedPrefs.getBoolean(PREF_KEY_NEW_WALLETAPP_COACHMARK_BALANCE, false)
}

fun isNewTokopointCoachmarkShown(context: Context): Boolean {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    return sharedPrefs.getBoolean(PREF_KEY_NEW_TOKOPOINT_COACHMARK_BALANCE, false)
}

fun setFalseNewWalletAppCoachmarkShown(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(PREF_KEY_NEW_WALLETAPP_COACHMARK_BALANCE, false).apply()
}

fun setFalseNewTokopointCoachmarkShown(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
    sharedPrefs.edit().putBoolean(PREF_KEY_NEW_TOKOPOINT_COACHMARK_BALANCE, false).apply()
}

fun isP1HomeCoachmarkDone(
    context: Context,
    isUseInboxRollence: Boolean,
    isUseWalletAppRollence: Boolean,
    isUseNavigationRollence: Boolean
): Boolean {
    var isDone = true
    if (
        (isUseInboxRollence && !isInboxCoachmarkShown(context)) ||
        (isUseWalletAppRollence && !isWalletAppCoachmarkShown(context) && !isWalletApp2CoachmarkShown(context)) ||
        (isUseNavigationRollence && !isNavigationCoachmarkShown(context))) {
        isDone = false
    }
    return isDone
}

