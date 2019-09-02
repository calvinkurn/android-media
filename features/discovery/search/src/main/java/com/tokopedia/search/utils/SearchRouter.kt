package com.tokopedia.search.utils

import android.content.Context

interface SearchRouter {
    fun getCartCount(context: Context): Int
}