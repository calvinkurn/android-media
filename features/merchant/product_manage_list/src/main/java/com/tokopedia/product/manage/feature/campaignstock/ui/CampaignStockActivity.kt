package com.tokopedia.product.manage.feature.campaignstock.ui

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.fragment.CampaignStockFragment
import kotlinx.android.synthetic.main.activity_campaign_stock.*
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
    }

    override fun getLayoutRes(): Int = R.layout.activity_campaign_stock

    override fun getParentViewResourceID(): Int = R.id.parent_view_campaign_stock

    override fun getNewFragment(): Fragment? = CampaignStockFragment.createInstance()

    private fun setupView() {
        window.decorView.setBackgroundColor(Color.WHITE)
        setupStatusBarTransparent()
        setupHeader()
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

    private fun setupHeader() {
        header_campaign_stock?.run {
            setBackgroundColor(Color.TRANSPARENT)
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }
}