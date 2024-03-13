package com.tokopedia.cart.view.pref

import android.content.Context
import android.content.SharedPreferences

class CartPreferences(val context: Context) {
    companion object {
        const val HAS_CLICKED_BUY_AGAIN_FLOATING_BUTTON = "has_clicked_buy_again_floating_button"
        const val CART_PREFERENCES = "cart_preferences"
    }

    private var sharedPrefs: SharedPreferences? = null

    init {
        sharedPrefs =
            context.getSharedPreferences(CART_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun setHasClickedBuyAgainFloatingButton() {
        sharedPrefs?.edit()?.putBoolean(HAS_CLICKED_BUY_AGAIN_FLOATING_BUTTON, true)?.apply()
    }

    fun hasClickedBuyAgainFloatingButton(): Boolean {
        return sharedPrefs?.getBoolean(HAS_CLICKED_BUY_AGAIN_FLOATING_BUTTON, false) ?: false
    }
}

