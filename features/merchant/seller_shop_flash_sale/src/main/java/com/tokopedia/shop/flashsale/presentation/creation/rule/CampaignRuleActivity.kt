package com.tokopedia.shop.flashsale.presentation.creation.rule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode

class CampaignRuleActivity : BaseSimpleActivity() {

    companion object {
        private const val BUNDLE_KEY_PAGE_MODE = "page_mode"

        @JvmStatic
        fun start(context: Context, mode: PageMode) {
            val starter = Intent(context, CampaignRuleActivity::class.java).apply {
                val extras = Bundle().apply {
                    putParcelable(BUNDLE_KEY_PAGE_MODE, mode)
                }
                putExtras(extras)
            }
            context.startActivity(starter)
        }
    }

    private val pageMode by lazy {
        intent?.extras?.getParcelable(BUNDLE_KEY_PAGE_MODE) as? PageMode ?: PageMode.CREATE
    }

    override fun getLayoutRes() = R.layout.ssfs_activity_campaign_rule
    override fun getNewFragment() = CampaignRuleFragment.newInstance()
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ssfs_activity_campaign_rule)
    }
}