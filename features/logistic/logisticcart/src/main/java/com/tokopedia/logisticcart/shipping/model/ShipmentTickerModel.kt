package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentTickerModel(
    val title: String = "",
    val type: Int = Ticker.TYPE_ANNOUNCEMENT,
    val description: String = "",
    val actionLabel: String = "",
    val position: ShipmentTickerPosition? = null,
    val actionType: ShipmentTickerActionType? = null
) : Parcelable, RatesViewModelType

enum class ShipmentTickerPosition(val key: String) {
    FREE_SHIPPING("free_shipping")
}

@Parcelize
sealed class ShipmentTickerActionType(val url: String) : Parcelable {
    data class AppUrl(val applink: String) : ShipmentTickerActionType(applink)
    data class WebUrl(val link: String) : ShipmentTickerActionType(link)
}
