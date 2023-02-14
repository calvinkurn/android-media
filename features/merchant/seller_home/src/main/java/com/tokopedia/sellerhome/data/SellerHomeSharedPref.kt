package com.tokopedia.sellerhome.data

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 15/11/22.
 */

class SellerHomeSharedPref @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val PREF_NAME = "SellerHomeSharedPref"
        private const val KEY_NEW_SELLER_WELCOMING_DIALOG = "new_seller_welcoming_dialog_%s"
        private const val KEY_NEW_SELLER_FIRST_ORDER_DIALOG = "new_seller_first_order_dialog_%s"
        private const val KEY_NEW_SELLER_WELCOMING_COACH_MARK = "new_seller_welcoming_coach_mark_%s"
        private const val KEY_PERSONA_ENTRY_POINT = "persona_entry_point_%s"
        private const val KEY_PERSONA_POPUP = "persona_entry_popup_%s"
    }

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getWelcomingDialogEligibility(userId: String): Boolean {
        val key = String.format(KEY_NEW_SELLER_WELCOMING_DIALOG, userId)
        return sharedPref.getBoolean(key, true)
    }

    fun makeWelcomingDialogNotEligible(userId: String) {
        val key = String.format(KEY_NEW_SELLER_WELCOMING_DIALOG, userId)
        sharedPref.edit().putBoolean(key, false).apply()
    }

    fun getFirstOrderDialogEligibility(userId: String): Boolean {
        val key = String.format(KEY_NEW_SELLER_FIRST_ORDER_DIALOG, userId)
        return sharedPref.getBoolean(key, true)
    }

    fun makeFirstOrderDialogNotEligible(userId: String) {
        val key = String.format(KEY_NEW_SELLER_FIRST_ORDER_DIALOG, userId)
        sharedPref.edit().putBoolean(key, false).apply()
    }

    fun getWelcomingCoachMarkEligibility(userId: String): Boolean {
        val key = String.format(KEY_NEW_SELLER_WELCOMING_COACH_MARK, userId)
        return sharedPref.getBoolean(key, true)
    }

    fun makeWelcomingCoachMarkNotEligible(userId: String) {
        val key = String.format(KEY_NEW_SELLER_WELCOMING_COACH_MARK, userId)
        sharedPref.edit().putBoolean(key, false).apply()
    }

    fun shouldShowPersonaEntryPoint(userId: String): Boolean {
        val key = String.format(KEY_PERSONA_ENTRY_POINT, userId)
        return sharedPref.getBoolean(key, false)
    }

    fun setPersonaEntryPointVisibility(userId: String, shouldVisible: Boolean) {
        val key = String.format(KEY_PERSONA_ENTRY_POINT, userId)
        sharedPref.edit().putBoolean(key, shouldVisible).apply()
    }

    fun shouldShowPersonaHomePopup(userId: String): Boolean {
        val key = String.format(KEY_PERSONA_POPUP, userId)
        return sharedPref.getBoolean(key, true)
    }

    fun markPersonaHomePopupShown(userId: String) {
        val key = String.format(KEY_PERSONA_POPUP, userId)
        sharedPref.edit().putBoolean(key, false).apply()
    }
}