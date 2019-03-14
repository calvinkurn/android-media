package com.tokopedia.product.detail

import android.content.Context
import com.tokopedia.transaction.common.TransactionRouter

interface ProductDetailRouter{
    fun getCartCount(context: Context): Int
    fun updateMarketplaceCartCounter(listener: TransactionRouter.CartNotificationListener)

    //temporary for feature flag
    fun goToOldProductDetailPage(context: Context, productId: String?, shopDomain: String?, productKey: String?, trackerAttribution: String?, trackerListName: String?)
}