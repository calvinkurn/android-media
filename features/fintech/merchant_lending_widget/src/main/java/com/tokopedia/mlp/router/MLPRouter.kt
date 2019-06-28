package com.tokopedia.mlp.router

import android.content.Context

interface MLPRouter {
    fun startMLPWebViewActivity(context: Context, url: String)
}