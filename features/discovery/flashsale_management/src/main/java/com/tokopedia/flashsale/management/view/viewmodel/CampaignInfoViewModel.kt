package com.tokopedia.flashsale.management.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flashsale.management.data.campaignlist.Campaign
import com.tokopedia.flashsale.management.data.campaignlist.Criteria
import com.tokopedia.flashsale.management.view.adapter.CampaignInfoAdapterTypeFactory

sealed class CampaignInfoViewModel: Visitable<CampaignInfoAdapterTypeFactory>{
    override fun type(typeFactory: CampaignInfoAdapterTypeFactory) = typeFactory.type(this)
}

class CampaignInfoHeaderViewModel(val campaign: Campaign): CampaignInfoViewModel()
class CampaignInfoSectionViewModel(val title: String): CampaignInfoViewModel()
class CampaignInfoCategoryViewModel(val criteria: Criteria): CampaignInfoViewModel()