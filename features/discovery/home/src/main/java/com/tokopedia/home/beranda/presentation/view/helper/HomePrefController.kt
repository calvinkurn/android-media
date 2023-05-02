package com.tokopedia.home.beranda.presentation.view.helper

import android.content.Context
import android.content.SharedPreferences

class HomePrefController(private val context: Context?) {
    companion object {
        const val PREF_KEY_HOME_REVAMP = "PREF_KEY_HOME_REVAMP"
        const val PREF_KEY_HOME_REVAMP_ATF_VARIANT = "PREF_KEY_HOME_REVAMP_ATF_VARIANT"
    }

    private var rollenceValue: String? = null
    private val sharedPrefs: SharedPreferences? by lazy {
        context?.getSharedPreferences(
            PREF_KEY_HOME_REVAMP,
            Context.MODE_PRIVATE
        )
    }

    fun setHomeRevampAtfVariant() {
        context?.run {
            try {
                HomeRollenceController.rollenceAtfValue.let {
                    if (rollenceValue != it) {
                        rollenceValue = it
                        sharedPrefs?.edit()?.putString(
                            PREF_KEY_HOME_REVAMP_ATF_VARIANT,
                            rollenceValue
                        )?.apply()
                    }
                }
            } catch (_: Exception) { }
        }
    }

    fun isUsingDifferentAtfRollenceVariant(): Boolean {
        return context?.run {
            val lastVariant = sharedPrefs?.getString(PREF_KEY_HOME_REVAMP_ATF_VARIANT, null)
            return lastVariant != HomeRollenceController.rollenceAtfValue
        } ?: true
    }
}
