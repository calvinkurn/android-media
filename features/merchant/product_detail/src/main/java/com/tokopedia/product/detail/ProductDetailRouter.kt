package com.tokopedia.product.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tokopedia.transaction.common.TransactionRouter
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam

interface ProductDetailRouter{
    fun getCartCount(context: Context): Int
    fun updateMarketplaceCartCounter(listener: TransactionRouter.CartNotificationListener)
}