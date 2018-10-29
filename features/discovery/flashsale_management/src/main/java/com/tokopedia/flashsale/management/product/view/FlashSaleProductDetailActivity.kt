
package com.tokopedia.flashsale.management.product.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.di.DaggerCampaignComponent
import com.tokopedia.flashsale.management.view.adapter.CampaignDetailFragmentPagerAdapter

class FlashSaleProductDetailActivity: BaseSimpleActivity(), HasComponent<CampaignComponent> {

    override fun getComponent() = DaggerCampaignComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getNewFragment(): Fragment {
        return FlashSaleProductDetailFragment.createInstance()
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context) =
                Intent(context, FlashSaleProductDetailActivity::class.java)
//                        .putExtra(EXTRA_PARAM_CAMPAIGN_ID, campaignId)
//                        .putExtra(EXTRA_PARAM_CAMPAIGN_URL, campaignUrl)

//        private const val EXTRA_PARAM_CAMPAIGN_ID = "campaign_id"
//        private const val EXTRA_PARAM_CAMPAIGN_URL = "campaign_url"
    }
}