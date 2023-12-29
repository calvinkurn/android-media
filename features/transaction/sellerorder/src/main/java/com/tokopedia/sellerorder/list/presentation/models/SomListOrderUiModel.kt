package com.tokopedia.sellerorder.list.presentation.models

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.ZERO
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
    val deadlineStyle: Int = Int.ZERO,
    val orderId: String = "",
    val orderProduct: List<OrderProduct> = listOf(),
    val productCount: Int = 0,
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
    val orderPlusData: OrderPlusData? = null,
    val multiSelectEnabled: Boolean = false,
    var isChecked: Boolean = false,
    var searchParam: String,
    var isOpen: Boolean = false
) : Visitable<SomListAdapterTypeFactory> {

    override fun type(typeFactory: SomListAdapterTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    fun isOrderWithCancellationRequest() = cancelRequest == 1 && cancelRequestStatus != 0

    fun hasActiveRequestCancellation() = cancelRequest != 0 && cancelRequestStatus != 0

    data class OrderProduct(
        val productId: String = "",
        val productName: String = "",
        val picture: String = "",
        val quantity: Int = 0
    )

    data class Button(
        val key: String = "",
        val displayName: String = "",
        val type: String = "",
        val url: String = "",
        val popUp: PopUp = PopUp()
    )

    data class OrderPlusData(
        val logoUrl: String? = null
    )
}
