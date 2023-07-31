package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductShipmentDetailModel(
    val originCity: String? = null,
    val weight: String? = null,
    val preOrderModel: PreOrderModel? = null
) : Parcelable, RatesViewModelType
