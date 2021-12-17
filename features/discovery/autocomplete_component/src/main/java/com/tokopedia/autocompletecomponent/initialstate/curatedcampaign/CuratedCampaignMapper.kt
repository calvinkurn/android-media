package com.tokopedia.autocompletecomponent.initialstate.curatedcampaign

import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateItem

fun InitialStateItem.convertToCuratedCampaignDataView(
    featureId: String,
    dimension90: String,
    keyword: String,
    trackingOption: Int,
): CuratedCampaignDataView {
    return CuratedCampaignDataView(
        baseItemInitialState = BaseItemInitialStateSearch(
            itemId = itemId,
            template = template,
            imageUrl = imageUrl,
            applink = applink,
            url = url,
            title = title,
            subtitle = subtitle,
            productId = itemId,
            type = type,
            featureId = featureId,
            position = 1,
            dimension90 = dimension90,
            keyword = keyword,
            campaignCode = campaignCode,
            componentId = componentId,
            trackingOption = trackingOption,
        )
    )
}