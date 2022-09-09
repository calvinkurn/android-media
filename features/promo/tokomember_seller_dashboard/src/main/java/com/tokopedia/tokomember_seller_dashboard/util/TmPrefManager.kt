package com.tokopedia.tokomember_seller_dashboard.util

import android.content.Context
import android.content.SharedPreferences

class TmPrefManager(var context: Context) {
    private val PREF_SHOP = "pref_shop"
    private val PREF_KEY_SHOP_ID = "shopId"
    private val PREF_KEY_CARD_ID = "cardId"

    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    init {
        pref = context.getSharedPreferences(PREF_SHOP, 0)
        pref?.let {
            editor = it.edit()
        }
    }

    var shopId: Int?
        get() = pref?.getInt(PREF_KEY_SHOP_ID, 0)
        set(value) {
            if (value != null) {
                editor?.putInt(PREF_KEY_SHOP_ID, value)
                editor?.apply()
            }
        }

    var cardId: Int?
        get() = pref?.getInt(PREF_KEY_CARD_ID, 0)
        set(value) {
            if (value != null) {
                editor?.putInt(PREF_KEY_CARD_ID, value)
                editor?.apply()
            }
        }

    fun clearPref(){
        editor?.clear()
    }

}