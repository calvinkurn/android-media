package com.tokopedia.shop.flashsale.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_shop_flash_sale.R

class CampaignDetailActivity : BaseSimpleActivity() {

    companion object {
        private const val BUNDLE_KEY_CAMPAIGN_ID = "campaign_id"
        private const val INVALID_CAMPAIGN_ID = -1L

        @JvmStatic
        fun start(context: Context, campaignId: Long) {
            val intent = Intent(context, CampaignDetailActivity::class.java).apply {
                putExtra(BUNDLE_KEY_CAMPAIGN_ID, campaignId)
            }
            context.startActivity(intent)
        }
    }


    private val campaignId by lazy {
        intent?.extras?.getLong(BUNDLE_KEY_CAMPAIGN_ID) ?: INVALID_CAMPAIGN_ID
    }

    override fun getLayoutRes() = R.layout.ssfs_activity_campaign_detail
    override fun getNewFragment() = CampaignDetailFragment.newInstance(campaignId)
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (INVALID_CAMPAIGN_ID == campaignId) finish()
        setContentView(R.layout.ssfs_activity_campaign_detail)
    }
}