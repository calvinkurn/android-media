package com.tokopedia.sellerappwidget.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

/**
 * Created By @ilhamsuaib on 22/11/20
 */

class SellerAppWidgetPreferencesImpl(context: Context) : SellerAppWidgetPreferences {

    companion object {
        private const val ONBOARDING_PREF = "seller_app_widget_preferences"
    }

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(
                ONBOARDING_PREF,
                Context.MODE_PRIVATE
        )
    }

    @SuppressLint("ApplySharedPref")
    override fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).commit()
    }

    override fun getString(key: String, defValue: String): String {
        return sharedPreferences.getString(key, defValue).orEmpty()
    }

    @SuppressLint("ApplySharedPref")
    override fun putLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).commit()
    }

    override fun getLong(key: String, defValue: Long): Long {
        return sharedPreferences.getLong(key, defValue)
    }
}