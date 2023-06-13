package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CashOnDeliveryProduct(
    var isCodAvailable: Int?,
    val codText: String?,
    val codPrice: Int?,
    val formattedPrice: String?,
    val tncText: String?,
    val tncLink: String?
) : Parcelable
