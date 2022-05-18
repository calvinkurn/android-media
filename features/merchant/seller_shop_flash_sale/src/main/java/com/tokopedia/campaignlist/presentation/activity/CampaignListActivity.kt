package com.tokopedia.campaignlist.presentation.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.seller_shop_flash_sale.R

class CampaignListActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ssfs_activity_campaign_list)
    }
}