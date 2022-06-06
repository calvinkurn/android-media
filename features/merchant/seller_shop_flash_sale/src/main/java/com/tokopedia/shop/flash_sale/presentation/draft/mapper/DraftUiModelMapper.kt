package com.tokopedia.shop.flash_sale.presentation.draft.mapper

import com.tokopedia.shop.flash_sale.common.constant.DraftConstant.MAX_DRAFT_COUNT
import com.tokopedia.shop.flash_sale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flash_sale.presentation.draft.uimodel.DraftItemModel
import com.tokopedia.shop.flash_sale.presentation.draft.uimodel.DraftUiModel

object DraftUiModelMapper {

    private const val DRAFT_DESC_SUFFIX = " Draft Produk"

    fun convertFromCampaignUiModel(campaignUiList: List<CampaignUiModel>) = DraftUiModel (
        isFull = campaignUiList.size >= MAX_DRAFT_COUNT,
        list = campaignUiList.map {
            DraftItemModel(
                id = it.campaignId,
                title = it.campaignName,
                description = it.summary.totalItem.toString() + DRAFT_DESC_SUFFIX,
                startDate = it.startDate,
                endDate = it.endDate
            )
        }
    )
}