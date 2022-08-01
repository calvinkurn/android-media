package com.tokopedia.shop.flashsale.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.constant.BundleConstant

class CampaignDetailActivity : BaseSimpleActivity() {

    companion object {
        private const val BUNDLE_KEY_CAMPAIGN_NAME = "campaign_name"
        private const val INVALID_CAMPAIGN_ID = -1L

        const val BUNDLE_KEY_CAMPAIGN_CANCELLATION_MESSAGE = "campaign_cancellation_message"
        const val REQUEST_CODE_CAMPAIGN_DETAIL = 101

        @JvmStatic
        fun buildIntent(context: Context, campaignId: Long, campaignName: String) :Intent {
            return Intent(context, CampaignDetailActivity::class.java).apply {
                putExtra(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
                putExtra(BUNDLE_KEY_CAMPAIGN_NAME, campaignName)
            }
        }
    }

    private val campaignId by lazy {
        intent?.getLongExtra(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, INVALID_CAMPAIGN_ID) ?: INVALID_CAMPAIGN_ID
    }
    private val campaignName by lazy {
        intent?.getStringExtra(BUNDLE_KEY_CAMPAIGN_NAME)
    }

    override fun getLayoutRes() = R.layout.ssfs_activity_campaign_detail
    override fun getNewFragment() = CampaignDetailFragment.newInstance(campaignId, campaignName)
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (INVALID_CAMPAIGN_ID == campaignId) finish()
        setContentView(R.layout.ssfs_activity_campaign_detail)
    }
}