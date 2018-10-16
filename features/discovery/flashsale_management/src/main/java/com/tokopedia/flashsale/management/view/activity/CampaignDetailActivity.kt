package com.tokopedia.flashsale.management.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.di.DaggerCampaignComponent
import com.tokopedia.flashsale.management.view.adapter.CampaignDetailFragmentPagerAdapter

class CampaignDetailActivity: BaseTabActivity(), HasComponent<CampaignComponent> {

    val titles by lazy {
        arrayOf(getString(R.string.label_flash_sale_info), getString(R.string.label_product_list))
    }

    override fun getViewPagerAdapter() = CampaignDetailFragmentPagerAdapter(supportFragmentManager, titles)

    override fun getPageLimit() = LIMIT_PAGER

    override fun getComponent() = DaggerCampaignComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.setupWithViewPager(viewPager)
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context, campaignId: Int) = Intent(context, CampaignDetailActivity::class.java)
                .putExtra(EXTRA_PARAM_CAMPAIGN_ID, campaignId)


        private const val LIMIT_PAGER = 2
        private const val EXTRA_PARAM_CAMPAIGN_ID = "campaign_id"
    }
}