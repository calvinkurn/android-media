package com.tokopedia.campaignlist.presentation.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.campaignlist.presentation.fragment.CampaignListFragment

class CampaignListActivity: BaseSimpleActivity() {

    override fun getNewFragment() = CampaignListFragment()

}