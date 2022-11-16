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
        private const val KEY_NEW_SELLER_WELCOMING_COACH_MARK = "new_seller_welcoming_coach_mark_%s"
    }

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getNewSellerWelcomingDialogEligibility(userId: String): Boolean {
        val key = String.format(KEY_NEW_SELLER_WELCOMING_DIALOG, userId)
        return sharedPref.getBoolean(key, true)
    }

    fun makeNewSellerWelcomingDialogNotEligible(userId: String) {
        val key = String.format(KEY_NEW_SELLER_WELCOMING_DIALOG, userId)
        sharedPref.edit().putBoolean(key, false).apply()
    }

    fun getNewSellerWelcomingCoachMarkEligibility(userId: String): Boolean {
        val key = String.format(KEY_NEW_SELLER_WELCOMING_COACH_MARK, userId)
        return sharedPref.getBoolean(key, true)
    }

    fun makeNewSellerWelcomingCoachMarkNotEligible(userId: String) {
        val key = String.format(KEY_NEW_SELLER_WELCOMING_COACH_MARK, userId)
        sharedPref.edit().putBoolean(key, false).apply()
    }
}