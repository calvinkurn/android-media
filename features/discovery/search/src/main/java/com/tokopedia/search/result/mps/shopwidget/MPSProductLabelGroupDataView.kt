package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS.Shop.Product.LabelGroup

data class MPSProductLabelGroupDataView(
    val position: String = "",
    val title: String = "",
    val type: String = "",
    val url: String = "",
) {

    companion object {
        fun create(labelGroup: LabelGroup) = MPSProductLabelGroupDataView(
            position = labelGroup.position,
            title = labelGroup.title,
            type = labelGroup.type,
            url = labelGroup.url,
        )
    }
}
