package com.tokopedia.sellerappwidget.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created By @ilhamsuaib on 16/11/20
 */

@Parcelize
data class OrderUiModel(
        val orders: List<OrderItemUiModel>? = emptyList(),
        val sellerOrderStatus: SellerOrderStatusUiModel? = SellerOrderStatusUiModel()
): Parcelable

@Parcelize
data class SellerOrderStatusUiModel(
        val newOrder: Int? = 0,
        val readyToShip: Int? = 0
): Parcelable

@Parcelize
data class OrderItemUiModel(
        val orderId: String? = "",
        val deadLineText: String? = "",
        val statusId: Int? = 0,
        val product: OrderProductUiModel? = null,
        val productCount: Int? = 0
): Parcelable