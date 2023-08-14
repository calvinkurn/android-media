package com.tokopedia.tokopedianow.buyercomm.presentation.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BuyerCommunicationData(
    val warehouseStatus: String = "",
    val operationHour: String = "",
    val shipmentOptions: List<ShipmentOptionData> = emptyList()
): Parcelable

