package com.tokopedia.product.detail.common

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.detail.common.postatc.PostAtc

object PostAtcHelper {

    /**
     * Additional Parameters for PostAtcActivity.kt
     */
    const val PARAM_POST_ATC = "postAtc"

    fun start(
        context: Context,
        productId: String,
        postAtc: PostAtc
    ) {
        val intent = getIntent(context, productId, postAtc)
        context.startActivity(intent)
    }

    fun getIntent(
        context: Context,
        productId: String,
        postAtc: PostAtc
    ): Intent {
        return RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.POST_ATC,
            productId
        ).apply {
            putExtra(PARAM_POST_ATC, postAtc)
        }
    }
}
