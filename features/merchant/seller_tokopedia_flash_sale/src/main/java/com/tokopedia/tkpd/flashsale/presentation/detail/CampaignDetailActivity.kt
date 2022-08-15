package com.tokopedia.tkpd.flashsale.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.presentation.landing.LandingContainerFragment

class CampaignDetailActivity : BaseSimpleActivity() {

    companion object {

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, CampaignDetailActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutRes() = R.layout.stfs_activity_landing
    override fun getNewFragment() = LandingContainerFragment.newInstance()
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stfs_activity_campaign_detail)
    }
}