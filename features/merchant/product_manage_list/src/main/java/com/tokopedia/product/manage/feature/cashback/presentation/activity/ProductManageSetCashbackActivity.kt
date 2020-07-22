package com.tokopedia.product.manage.feature.cashback.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.EXTRA_CASHBACK_SHOP_ID
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.PARAM_SET_CASHBACK_PRODUCT_PRICE
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.PARAM_SET_CASHBACK_VALUE

class ProductManageSetCashbackActivity: BaseSimpleActivity() {

    companion object {
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_PRODUCT_NAME = "product_name"
        const val PARAM_CASHBACK = "cashback"
        const val PARAM_PRICE = "price"

        fun createIntent(context: Context, productId: String?, productName: String?, cashback: Int?, price: String?) : Intent {
            val intent = Intent(context, ProductManageSetCashbackActivity::class.java)
            val bundle = Bundle()
            bundle.putString(PARAM_PRODUCT_ID, productId)
            bundle.putString(PARAM_PRODUCT_NAME, productName)
            bundle.putInt(PARAM_CASHBACK, cashback ?: 0)
            bundle.putString(PARAM_PRICE, price)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun getNewFragment(): Fragment? {
        var productId = "0"
        var cashback = 0
        var productName = ""
        var price = ""
        val uri = intent.data
        if (uri != null) {
            val segments = uri.pathSegments
            productId = segments[segments.size - 1]
            productName = uri.getQueryParameter(PARAM_PRODUCT_NAME).toBlankOrString()
            cashback = uri.getQueryParameter(PARAM_SET_CASHBACK_VALUE).toIntOrZero()
            price = uri.getQueryParameter(PARAM_SET_CASHBACK_PRODUCT_PRICE) ?: ""
        } else {
            intent.extras?.let {
                productId = it.getString(PARAM_PRODUCT_ID, "")
                productName = it.getString(PARAM_PRODUCT_NAME, "")
                cashback = it.getInt(PARAM_CASHBACK)
                price = it.getString(PARAM_PRICE, "")
            }
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