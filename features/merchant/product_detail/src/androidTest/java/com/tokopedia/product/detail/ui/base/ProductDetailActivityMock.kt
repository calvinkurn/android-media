package com.tokopedia.product.detail.ui.base

import android.content.Context
import android.content.Intent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.ui.di.ProductDetailTestComponent
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment

/**
 * Created by Yehezkiel on 08/04/21
 */

class ProductDetailActivityMock : ProductDetailActivity() {

    companion object {
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PRODUCT_DETAIL_TAG = "productDetailTag"

        @JvmStatic
        fun createIntent(context: Context, productId: String) = Intent(context, ProductDetailActivityMock::class.java).apply {
            putExtra(PARAM_PRODUCT_ID, productId)
        }
    }

    lateinit var productDetailTestComponent: ProductDetailTestComponent

    override fun inflateFragment() {
        // Don't inflate fragment immediately
    }

    override fun getComponent(): ProductDetailComponent {
        return productDetailTestComponent
    }

    fun setupTestFragment(productDetailTestComponent: ProductDetailTestComponent?) {
        productDetailTestComponent?.let {
            this.productDetailTestComponent = it
            supportFragmentManager.beginTransaction()
                    .replace(parentViewResourceID, newFragment, tagFragment)
                    .commit()
        }
    }

    override fun getTagFragment(): String {
        return PRODUCT_DETAIL_TAG
    }

    fun getPdpFragment():DynamicProductDetailFragment {
        return supportFragmentManager.findFragmentByTag(tagFragment) as DynamicProductDetailFragment
    }

}