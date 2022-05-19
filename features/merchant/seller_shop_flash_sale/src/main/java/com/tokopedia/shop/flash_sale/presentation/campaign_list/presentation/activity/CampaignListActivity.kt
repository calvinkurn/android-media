package com.tokopedia.shop.flash_sale.presentation.campaign_list.presentation.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.flash_sale.presentation.campaign_list.presentation.fragment.CampaignListFragment

class CampaignListActivity: BaseSimpleActivity() {

    override fun getNewFragment() = CampaignListFragment()

}