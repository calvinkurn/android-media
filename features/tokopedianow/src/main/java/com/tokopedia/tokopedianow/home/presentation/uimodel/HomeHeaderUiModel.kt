package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.buyercomm.presentation.data.BuyerCommunicationData
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory
import com.tokopedia.tokopedianow.home.presentation.model.HomeHeaderBackgroundData

data class HomeHeaderUiModel(
    val id: String,
    val title: String = "",
    val shippingHint: String = "",
    val logoUrl: String = "",
    val background: HomeHeaderBackgroundData = HomeHeaderBackgroundData(),
    val buyerCommunication: BuyerCommunicationData = BuyerCommunicationData(),
    val warehouses: List<WarehouseData> = emptyList(),
    val state: HomeLayoutItemState = HomeLayoutItemState.LOADING
) : HomeLayoutUiModel(id) {

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getChangePayload(newModel: HomeLayoutUiModel): Any {
        val newItem = newModel as HomeHeaderUiModel
        return this != newItem
    }
}
