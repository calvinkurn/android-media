package com.tokopedia.logisticcart.scheduledelivery.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorRatesDetailData
import kotlinx.parcelize.Parcelize

@Parcelize
class DeliveryService(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("title_label")
    val titleLabel: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("available")
    val available: Boolean = false,
    @SerializedName("hidden")
    val hidden: Boolean = false,
    @SerializedName("error")
    val error: ErrorRatesDetailData = ErrorRatesDetailData(),
    @SerializedName("delivery_products")
    val deliveryProducts: List<DeliveryProduct> = arrayListOf(),
) : Parcelable
