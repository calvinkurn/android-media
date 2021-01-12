package com.tokopedia.product.manage.feature.campaignstock.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.fragment.CampaignStockFragment
import timber.log.Timber

class CampaignStockActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context,
                         shopId: String,
                         productId: Array<String>): Intent {
            return Intent(context, CampaignStockActivity::class.java).apply {
                putExtra(SHOP_ID, shopId)
                putExtra(PRODUCT_ID, productId)
            }
        }

        const val SHOP_ID = "extra_shop_id"
        const val PRODUCT_ID = "extra_product_id"

        private const val PRODUCT_ID_SEGMENT_INDEX = 1
        private const val SHOP_ID_SEGMENT_INDEX = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
    }

    override fun getLayoutRes(): Int = R.layout.activity_campaign_stock

    override fun getParentViewResourceID(): Int = R.id.parent_view_campaign_stock

    override fun getNewFragment(): Fragment? {
        setupApplinkAttribute()
        return CampaignStockFragment.createInstance()
    }

    private fun setupView() {
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        setupStatusBarTransparent()
    }

    private fun setupStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.design.R.color.transparent))
            } catch (ex: Resources.NotFoundException) {
                Timber.e(ex)
            }
        }
    }


    private fun setupApplinkAttribute() {
        intent?.data?.let { uri ->
            uri.getProductIds()?.let { productIds ->
                uri.getShopId()?.let { shopId ->
                    intent?.run {
                        putExtra(PRODUCT_ID, productIds)
                        putExtra(SHOP_ID, shopId)
                    }
                }
            }
        }
    }

    private fun Uri.getProductIds(): Array<String>? {
        pathSegments.getOrNull(PRODUCT_ID_SEGMENT_INDEX)?.let { productId ->
            return arrayOf(productId)
        }
        return null
    }

    private fun Uri.getShopId(): String? = pathSegments.getOrNull(SHOP_ID_SEGMENT_INDEX)

}