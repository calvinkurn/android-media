package com.tokopedia.product.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment
import com.tokopedia.utils.view.ViewUtils.screenShotAndSave


/**
 * Created by Yehezkiel on 10/01/21
 * Activity mock for testing, use this if you want to adding new function on activity
 */
class ProductDetailActivityCommonTest : BaseSimpleActivity() {

    var productId: String = ""

    companion object {
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PRODUCT_DETAIL_TAG = "productDetailTag"

        @JvmStatic
        fun createIntent(context: Context, productId: String) = Intent(context, ProductDetailActivityCommonTest::class.java).apply {
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
        Handler(Looper.getMainLooper()).post {
            screenShotAndSave(window.decorView, "", screenshotName)
        }
    }

    fun getLastPositionIndex() : Int {
        val fragment = supportFragmentManager.findFragmentByTag(PRODUCT_DETAIL_TAG) as DynamicProductDetailFragment

        return fragment.adapter.dataSize - 1
    }
}