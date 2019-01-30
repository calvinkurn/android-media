package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.fragment.ProductFullDescriptionFragment

class ProductFullDescriptionActivity: BaseSimpleActivity() {

    companion object {
        private const val PARAM_PRODUCT_NAME = "product_name"
        private const val PARAM_PRODUCT_PRICE = "product_price"
        private const val PARAM_PRODUCT_SHOP = "product_shop"
        private const val PARAM_PRODUCT_IMAGE = "product_image"
        private const val PARAM_PRODUCT_DESCR = "product_descr"
        private const val PARAM_PRODUCT_VIDS = "product_vids"
        private const val PARAM_IS_OS = "is_os"

        fun createIntent(context: Context, productName: String, productPrice: Int, shopName: String,
                         productImage: String, productDescr: String, productVids: List<String>,
                         isOS: Boolean = false): Intent {

            return Intent(context, ProductFullDescriptionActivity::class.java).apply {
                putExtra(PARAM_PRODUCT_NAME, productName)
                putExtra(PARAM_PRODUCT_PRICE, productPrice)
                putExtra(PARAM_PRODUCT_SHOP, shopName)
                putExtra(PARAM_PRODUCT_IMAGE, productImage)
                putExtra(PARAM_PRODUCT_DESCR, productDescr)
                putExtra(PARAM_PRODUCT_VIDS, productVids.toTypedArray())
                putExtra(PARAM_IS_OS, isOS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close_default))
    }

    override fun getNewFragment(): Fragment = ProductFullDescriptionFragment
            .createInstance(intent.getStringExtra(PARAM_PRODUCT_NAME) ?: "",
                    intent.getIntExtra(PARAM_PRODUCT_PRICE, 0),
                    intent.getStringExtra(PARAM_PRODUCT_SHOP) ?: "",
                    intent.getStringExtra(PARAM_PRODUCT_IMAGE) ?: "",
                    intent.getStringExtra(PARAM_PRODUCT_DESCR) ?: "",
                    intent.getStringArrayExtra(PARAM_PRODUCT_VIDS)?.toList() ?: listOf(),
                    intent.getBooleanExtra(PARAM_IS_OS, false))
}