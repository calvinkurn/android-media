package com.tokopedia.loginregister.shopcreation.util

import android.content.Context

object ShopCreationUtils {

    private const val SHOP_CREATION_PREF = "temp_shop_creation"
    private const val KEY_SHOP_CREATION_PENDING = "key_shop_pending"

    fun storeShopStatus(context: Context, isShopPending: Boolean) {
        context.getSharedPreferences(SHOP_CREATION_PREF, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_SHOP_CREATION_PENDING, isShopPending)
            .apply()
    }

    fun isShopPending(context: Context): Boolean =
        context.getSharedPreferences(SHOP_CREATION_PREF, Context.MODE_PRIVATE).getBoolean(KEY_SHOP_CREATION_PENDING, false)
}
