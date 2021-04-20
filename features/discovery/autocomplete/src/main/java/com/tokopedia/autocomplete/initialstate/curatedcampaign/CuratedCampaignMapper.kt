package com.tokopedia.autocomplete.initialstate.curatedcampaign

import com.tokopedia.autocomplete.initialstate.InitialStateItem

fun InitialStateItem.convertToCuratedCampaignDataView(featureId: String): CuratedCampaignDataView {
    return CuratedCampaignDataView(
            template = template,
            imageUrl = imageUrl,
            applink = applink,
            url = url,
            title = title,
            subtitle = subtitle,
            productId = itemId,
            type = type,
            featureId = featureId
    )
}