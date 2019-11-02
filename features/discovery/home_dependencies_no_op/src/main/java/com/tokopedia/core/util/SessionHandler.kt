package com.tokopedia.core.util

import android.content.Context

class SessionHandler(
        private val context: Context
) {

    companion object {
        @JvmStatic
        fun isUserHasShop(context: Context): Boolean {
            return false
        }

        @JvmStatic
        fun getShopDomain(context: Context): String {
            return ""
        }
    }
}