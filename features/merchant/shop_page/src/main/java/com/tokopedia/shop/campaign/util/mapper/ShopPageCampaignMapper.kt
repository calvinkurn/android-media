package com.tokopedia.shop.campaign.util.mapper

import com.tokopedia.shop.common.data.model.ShopPageWidgetLayoutUiModel
import com.tokopedia.shop.common.data.model.WidgetIdList

object ShopPageCampaignMapper {
    fun mapToListWidgetLayoutData(listWidgetLayoutData: List<WidgetIdList>): List<ShopPageWidgetLayoutUiModel> {
        return listWidgetLayoutData.map {
            ShopPageWidgetLayoutUiModel(
                it.widgetId,
                it.widgetMasterId,
                it.widgetType,
                it.widgetName
            )
        }
    }
}