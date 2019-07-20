package com.tokopedia.mlp.router

import android.content.Context
import android.content.Intent

interface MLPRouter {
    fun getSellerWebViewIntent(context: Context, webViewUrl: String): Intent
}
