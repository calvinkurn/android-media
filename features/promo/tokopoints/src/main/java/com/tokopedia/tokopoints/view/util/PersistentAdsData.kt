package com.tokopedia.tokopoints.view.util

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.kotlin.extensions.clear

class PersistentAdsData(val context: Context) {
    private fun getSharedPReference() : SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getAdsSet(): MutableSet<String>? {
        return getSharedPReference().getStringSet(KEY_ADS, HashSet<String>())
    }

    fun setAdsSet(value: HashSet<String?>) {
        getSharedPReference().edit().putStringSet(KEY_ADS, value).apply()
    }

    fun deletePreference(){
        getSharedPReference().clear()
    }

    companion object {
        private const val PREF_NAME = "merchantadsevent.pref"
        private const val KEY_ADS = "adview"
    }
}