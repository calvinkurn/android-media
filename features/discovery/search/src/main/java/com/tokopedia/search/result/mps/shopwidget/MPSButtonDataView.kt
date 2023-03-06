package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.search.result.mps.domain.model.MPSModel.AceSearchShopMPS.Shop.Button

data class MPSButtonDataView(
    val text: String = "",
    val applink: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
) {

    companion object {
        fun create(button: Button): MPSButtonDataView = MPSButtonDataView(
            text = button.text,
            applink = button.applink,
            componentId = button.componentId,
            trackingOption = button.trackingOption,
        )
    }
}
