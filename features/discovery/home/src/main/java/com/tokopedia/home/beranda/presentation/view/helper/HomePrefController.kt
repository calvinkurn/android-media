package com.tokopedia.home.beranda.presentation.view.helper

import android.content.Context
import android.content.SharedPreferences

class HomePrefController(private val context: Context?) {
    companion object {
        const val PREF_KEY_HOME_REVAMP = "PREF_KEY_HOME_REVAMP"
        const val PREF_KEY_HOME_REVAMP_ATF_VARIANT = "PREF_KEY_HOME_REVAMP_ATF_VARIANT"
    }

    fun setHomeRevampAtfVariant() {
        context?.run {
            val sharedPrefs: SharedPreferences = context.getSharedPreferences(
                PREF_KEY_HOME_REVAMP,
                Context.MODE_PRIVATE
            )
            sharedPrefs.edit().putString(PREF_KEY_HOME_REVAMP_ATF_VARIANT, HomeRollenceController.rollenceAtfValue).apply()
        }
    }

    fun isUsingDifferentAtfRollenceVariant(): Boolean {
        return context?.run {
            val sharedPrefs: SharedPreferences = context.getSharedPreferences(
                PREF_KEY_HOME_REVAMP,
                Context.MODE_PRIVATE
            )
            val lastVariant = sharedPrefs.getString(PREF_KEY_HOME_REVAMP_ATF_VARIANT, null)
            return lastVariant != HomeRollenceController.rollenceAtfValue
        } ?: true
    }
}
