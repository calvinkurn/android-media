package com.tokopedia.flashsale.management.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flashsale.management.data.campaignlabel.CampaignStatus
import com.tokopedia.flashsale.management.view.adapter.CampaignAdapterTypeFactory

class CampaignStatusListViewModel : Visitable<CampaignAdapterTypeFactory> {

    var campaignStatusList = ArrayList<CampaignStatus>()

    override fun type(typeFactory: CampaignAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}