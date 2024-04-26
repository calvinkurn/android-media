package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentTickerModel(
    val title: String = "",
    val type: ShipmentTickerType = ShipmentTickerType.ANNOUNCEMENT,
    val description: String = "",
    val actionLabel: String = "",
    val position: ShipmentTickerPosition? = null,
    val actionType: ShipmentTickerActionType? = null
) : Parcelable, RatesViewModelType

enum class ShipmentTickerType {
    ANNOUNCEMENT, WARNING, ERROR
}

enum class ShipmentTickerPosition(val key: String) {
    FREE_SHIPPING("free_shipping")
}

@Parcelize
sealed class ShipmentTickerActionType(val url: String) : Parcelable {
    data class AppUrl(val applink: String) : ShipmentTickerActionType(applink)
    data class WebUrl(val link: String) : ShipmentTickerActionType(link)
}
