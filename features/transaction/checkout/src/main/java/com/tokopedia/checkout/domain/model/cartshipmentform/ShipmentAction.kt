package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentAction(
    val action: String = "",
    val popup: ShipmentActionPopup = ShipmentActionPopup()
) : Parcelable {

    @Parcelize
    data class ShipmentActionPopup(
        val title: String = "",
        val body: String = "",
        val primaryButton: String = "",
        val secondaryButton: String = ""
    ) : Parcelable
}
