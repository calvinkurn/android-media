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
        it.labelName = this.name
        it.statusId = this.id
    }
}

fun CampaignStatus.convertIdtoCommaString(): String {
    return this.id.joinToString(",") { it.toString() }
}

fun Campaign.toListCampaignInfoViewModel(): List<CampaignInfoViewModel> {
    val list = mutableListOf<CampaignInfoViewModel>()
    list.add(CampaignInfoHeaderViewModel(this))
    list.add(CampaignInfoSectionViewModel("Kriteria Produk"))
    list.addAll(criteria.map { CampaignInfoCategoryViewModel(it)})
    list.add(CampaignInfoShopCriteriaViewModel(sellerTypes.map { it.title }.joinToString(", "),
            minSellerReputation, maxSellerReputation, minCancellationRate, maxCancellationRate,
            logistics.map { it.logisticName }))
    list.add(CampaignInfoDescriptionViewModel(description))
    if (minTransaction.isNotBlank() || promoCode.isNotBlank())
        list.add(CampaignInfoPromoViewModel(minTransaction, promoCode))
    list.add(CampaignInfoTnCViewModel(tnc, tncLastUpdated))
    return list
}