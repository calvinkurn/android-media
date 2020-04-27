package com.tokopedia.product.manage.feature.cashback.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.EXTRA_CASHBACK_SHOP_ID
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.PARAM_SET_CASHBACK_PRODUCT_PRICE
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.PARAM_SET_CASHBACK_VALUE

class ProductManageSetCashbackActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        var productId = "0"
        var cashback = 0
        var productName = ""
        var price = ""
        val uri = intent.data
        if (uri != null) {
            val segments = uri.pathSegments
            productId = segments[segments.size - 2]
            productName = segments[segments.size - 1]
            cashback = uri.getQueryParameter(PARAM_SET_CASHBACK_VALUE).toIntOrZero()
            price = uri.getQueryParameter(PARAM_SET_CASHBACK_PRODUCT_PRICE) ?: ""
        }
        val shopId = intent?.getStringExtra(EXTRA_CASHBACK_SHOP_ID) ?: ""
        if (shopId != "") {
            return ProductManageSetCashbackFragment.createInstance(productId, cashback, productName, price, shopId)
        }
        return ProductManageSetCashbackFragment.createInstance(productId, cashback, productName, price)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val f = fragment
        if(f != null && f is ProductManageSetCashbackFragment) {
            f.setTrackerOnBackPressed()
        }
    }

}