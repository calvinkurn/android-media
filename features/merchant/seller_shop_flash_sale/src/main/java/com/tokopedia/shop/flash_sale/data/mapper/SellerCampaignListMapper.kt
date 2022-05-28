package com.tokopedia.shop.flash_sale.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flash_sale.common.constant.DateConstant
import com.tokopedia.shop.flash_sale.common.extension.formatTo
import com.tokopedia.shop.flash_sale.common.extension.toDate
import com.tokopedia.shop.flash_sale.data.response.GetSellerCampaignListResponse
import com.tokopedia.shop.flash_sale.domain.entity.CampaignMeta
import com.tokopedia.shop.flash_sale.domain.entity.CampaignUiModel
import javax.inject.Inject

class SellerCampaignListMapper @Inject constructor() {

    fun map(data: GetSellerCampaignListResponse): CampaignMeta {
        val campaigns = data.getSellerCampaignList.campaign.map {
            CampaignUiModel(
                it.campaignId.toLongOrZero(),
                it.campaignName,
                it.endDate.toDate().formatTo(DateConstant.DATE),
                it.endDate.toDate().formatTo(DateConstant.TIME),
                it.isCancellable,
                it.isShareable,
                it.notifyMeCount,
                it.startDate.toDate().formatTo(DateConstant.DATE),
                it.startDate.toDate().formatTo(DateConstant.TIME),
                it.statusId.toIntOrZero(),
                it.thematicParticipation,
                CampaignUiModel.ProductSummary(
                    it.productSummary.totalItem,
                    it.productSummary.soldItem,
                    it.productSummary.reservedProduct,
                    it.productSummary.submittedProduct,
                    it.productSummary.deletedProduct,
                    it.productSummary.visibleProductCount
                )
            )
        }
        return CampaignMeta(
            data.getSellerCampaignList.totalCampaign,
            data.getSellerCampaignList.totalCampaignActive,
            data.getSellerCampaignList.totalCampaignFinished,
            campaigns
        )
    }

}