package com.tokopedia.catalogcommon.listener

import com.tokopedia.catalogcommon.uimodel.TopFeaturesUiModel

interface TopFeatureListener {
    fun onTopFeatureImpression(
        items: List<TopFeaturesUiModel.ItemTopFeatureUiModel>,
        widgetName: String
    )
}
