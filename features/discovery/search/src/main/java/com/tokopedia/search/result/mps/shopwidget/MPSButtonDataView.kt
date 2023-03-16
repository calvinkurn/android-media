package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS.Shop.Button

data class MPSButtonDataView(
    val name: String = "",
    val text: String = "",
    val applink: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
) {

    fun isPrimary() = name == PRIMARY

    fun isSecondary() = name == SECONDARY

    companion object {
        const val PRIMARY = "primary"
        const val SECONDARY = "secondary"

        fun create(button: Button): MPSButtonDataView = MPSButtonDataView(
            name = button.name,
            text = button.text,
            applink = button.applink,
            componentId = button.componentId,
            trackingOption = button.trackingOption,
        )
    }
}
