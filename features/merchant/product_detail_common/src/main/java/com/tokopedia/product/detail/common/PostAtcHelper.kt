package com.tokopedia.product.detail.common

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.detail.common.postatc.PostAtcParams

object PostAtcHelper {

    /**
     * Additional Parameters for PostAtcActivity.kt
     */
    const val POST_ATC_PARAMS = "post_atc_params"
    const val POST_ATC_PARAMS_CACHE_ID = "post_atc_params_cache_id"

    fun start(
        context: Context,
        productId: String,
        postAtcParams: PostAtcParams
    ) {
        val cacheManager = SaveInstanceCacheManager(context, true)
        cacheManager.put(POST_ATC_PARAMS, postAtcParams)
        getIntent(context, productId) { intent ->
            intent.putExtra(POST_ATC_PARAMS_CACHE_ID, cacheManager.id)
            context.startActivity(intent)
        }
    }

    fun getIntent(
        context: Context,
        productId: String,
        onIntent: (Intent) -> Unit
    ) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.POST_ATC,
            productId
        )
        onIntent.invoke(intent)
    }
}
