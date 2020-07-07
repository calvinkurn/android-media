package com.tokopedia.product.manage.feature.campaignstock.ui

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.feature.campaignstock.ui.fragment.CampaignStockFragment

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

    override fun getNewFragment(): Fragment? = CampaignStockFragment.createInstance()
}