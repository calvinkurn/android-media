package com.tokopedia.sellerorder.list.presentation.models

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.common.domain.model.TickerInfo
import com.tokopedia.sellerorder.common.presenter.model.PopUp
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory

data class SomListOrderUiModel(
        val cancelRequest: Int = 0,
        val cancelRequestNote: String = "",
        val cancelRequestOriginNote: String = "",
        val cancelRequestTime: String = "",
        val cancelRequestStatus: Int = 0,
        val deadlineColor: String = "",
        val deadlineText: String = "",
        val orderId: String = "",
        val orderProduct: List<OrderProduct> = listOf(),
        val orderResi: String = "",
        val orderStatusId: Int = 0,
        val status: String = "",
        val statusColor: String = "",
        val statusIndicatorColor: String = "",
        val destinationProvince: String = "",
        val courierName: String = "",
        val courierProductName: String = "",
        val preOrderType: Int = 0,
        val buyerName: String = "",
        val tickerInfo: TickerInfo = TickerInfo(),
        val buttons: List<Button> = emptyList(),
        var isChecked: Boolean = false,
        var searchParam: String,
        var isOpen: Boolean = false
) : Visitable<SomListAdapterTypeFactory> {
    override fun type(typeFactory: SomListAdapterTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    data class OrderProduct(
            val productId: String = "",
            val productName: String = "",
            val picture: String = ""
    )

    data class Button(
            val key: String = "",
            val displayName: String = "",
            val type: String = "",
            val url: String = "",
            val popUp: PopUp = PopUp()
    )
}