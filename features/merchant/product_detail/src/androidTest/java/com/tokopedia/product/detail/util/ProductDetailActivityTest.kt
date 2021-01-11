package com.tokopedia.product.detail.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment
import com.tokopedia.utils.view.ViewUtils.screenShotAndSave

/**
 * Created by Yehezkiel on 10/01/21
 */
class ProductDetailActivityTest : BaseSimpleActivity() {

    var productId: String = ""

    companion object {
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PRODUCT_DETAIL_TAG = "productDetailTag"

        @JvmStatic
        fun createIntent(context: Context, productId: String) = Intent(context, ProductDetailActivityTest::class.java).apply {
            putExtra(PARAM_PRODUCT_ID, productId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        bundle?.let {
            productId = it.getString(PARAM_PRODUCT_ID) ?: ""
        }

        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int = R.layout.activity_product_detail

    override fun getTagFragment(): String {
        return PRODUCT_DETAIL_TAG
    }

    override fun getNewFragment(): Fragment? = DynamicProductDetailFragment.newInstance(productId, "", "",
            "", false,
            isAffiliate = false, trackerAttribution = "",
            trackerListName = "", affiliateString = "", deeplinkUrl = "", layoutId = "")

    fun takeScreenShot(screenshotName: String) {
        screenShotAndSave(window.decorView, "dark_mode", screenshotName)
    }
}