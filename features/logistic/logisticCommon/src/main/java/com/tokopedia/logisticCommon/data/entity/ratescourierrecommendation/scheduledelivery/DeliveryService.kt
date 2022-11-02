package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
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
    val error: Error = Error(),
    @SerializedName("delivery_products")
    val deliveryProducts: List<DeliveryProduct> = arrayListOf(),
) : Parcelable
