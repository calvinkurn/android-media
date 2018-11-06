package com.tokopedia.flashsale.management.ekstension

import com.tokopedia.flashsale.management.data.campaignlabel.CampaignStatus
import com.tokopedia.flashsale.management.data.campaignlist.Campaign
import com.tokopedia.flashsale.management.view.viewmodel.*

fun Campaign.toCampaignViewModel(): CampaignViewModel {
    return CampaignViewModel().also {
        it.id = this.campaignId
        it.name = this.name
        it.campaignPeriod = this.campaignPeriod
        it.status = this.status
        it.campaignType = this.campaignType
        it.cover = this.cover
        it.campaignUrl = this.dashboardUrl
    }
}

fun CampaignStatus.toCampaignStatusViewModel(): CampaignStatusViewModel {
    return CampaignStatusViewModel().also {
        it.labelName = this.label_name
        it.status_id = this.status_id
    }
}

fun CampaignStatusViewModel.convertIdtoCommaString(): String {
    return this.status_id.joinToString(",") { it.toString() }
}

fun Campaign.toListCampaignInfoViewModel(): List<CampaignInfoViewModel> {
    val list = mutableListOf<CampaignInfoViewModel>()
    list.add(CampaignInfoHeaderViewModel(this))
    list.add(CampaignInfoSectionViewModel("Kriteria Produk"))
    list.addAll(criteria.map { CampaignInfoCategoryViewModel(it)})
    return list
}