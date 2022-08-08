package com.tokopedia.shop.flashsale.presentation.creation.rule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.constant.BundleConstant
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode

class CampaignRuleActivity : BaseSimpleActivity() {

    companion object {
        private const val INVALID_CAMPAIGN_ID = -1L

        @JvmStatic
        fun start(context: Context, campaignId: Long, pageMode: PageMode) {
            val starter = Intent(context, CampaignRuleActivity::class.java).apply {
                val extras = Bundle().apply {
                    putLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
                    putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                }
                putExtras(extras)
            }
            context.startActivity(starter)
        }
    }

    private val pageMode by lazy {
        intent?.extras?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) ?: PageMode.CREATE
    }

    private val campaignId by lazy {
        intent?.extras?.getLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID) ?: INVALID_CAMPAIGN_ID
    }

    override fun getLayoutRes() = R.layout.ssfs_activity_campaign_rule
    override fun getNewFragment() = CampaignRuleFragment.newInstance(campaignId, pageMode)
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (INVALID_CAMPAIGN_ID == campaignId) finish()
        setContentView(R.layout.ssfs_activity_campaign_rule)
    }
}