package com.tokopedia.flashsale.management.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flashsale.management.data.campaignlist.Campaign
import com.tokopedia.flashsale.management.data.campaignlist.Criteria
import com.tokopedia.flashsale.management.view.adapter.CampaignInfoAdapterTypeFactory

sealed class CampaignInfoViewModel: Visitable<CampaignInfoAdapterTypeFactory>{
    override fun type(typeFactory: CampaignInfoAdapterTypeFactory) = typeFactory.type(this)
}

data class CampaignInfoHeaderViewModel(val campaign: Campaign): CampaignInfoViewModel()
data class CampaignInfoSectionViewModel(val title: String): CampaignInfoViewModel()
data class CampaignInfoCategoryViewModel(val criteria: Criteria): CampaignInfoViewModel()
data class CampaignInfoShopCriteriaViewModel(val shopType:String = "-",
                                             val shopMinReputation: String,
                                             val shopMaxReputation: String,
                                             val shopMinCancellationRate: Int,
                                             val shopMaxCancellationRate: Int,
                                             val courierNames: List<String>): CampaignInfoViewModel()
data class CampaignInfoDescriptionViewModel(val description: String): CampaignInfoViewModel()
data class CampaignInfoPromoViewModel(val minTransaction: String,
                                      val promoCode: String): CampaignInfoViewModel()
data class CampaignInfoTnCViewModel(val tnc: String, val tncLastUpdated:String): CampaignInfoViewModel()