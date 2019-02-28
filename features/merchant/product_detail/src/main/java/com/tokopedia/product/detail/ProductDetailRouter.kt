package com.tokopedia.product.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tokopedia.transaction.common.TransactionRouter
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam

interface ProductDetailRouter{
    fun getCartIntent(context: Context): Intent
    fun getProductTalk(context: Context, productId: String): Intent
    fun getProductReputationIntent(context: Context, productId: String, productName: String): Intent
    fun getShoProductListIntent(context: Context, shopId: String, keyword: String, etalaseId: String): Intent
    fun getShopPageIntent(context: Context, shopId: String): Intent
    fun goToEditProduct(context: Context, isEdit: Boolean, productId: String): Intent
    fun getIntermediaryIntent(context: Context, categoryId: String): Intent //only for non seller-app
    fun getCartCount(context: Context): Int
    fun getExpressCheckoutIntent(activity: Activity, atcRequestParam: AtcRequestParam): Intent
    fun updateMarketplaceCartCounter(listener: TransactionRouter.CartNotificationListener)
}