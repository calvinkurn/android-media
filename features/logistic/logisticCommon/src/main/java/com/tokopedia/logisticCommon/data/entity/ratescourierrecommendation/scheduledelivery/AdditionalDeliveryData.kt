package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class AdditionalDeliveryData(
    @SerializedName("delivery_type")
    val deliveryType: Int = 0,
    @SerializedName("available")
    val available: Boolean = false,
    @SerializedName("hidden")
    val hidden: Boolean = false,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("text")
    val text: String = "",
    @SerializedName("notice")
    val notice: Notice = Notice(),
    @SerializedName("ticker")
    val ticker: Ticker? = null,
    @SerializedName("category_ids")
    val categoryIds: List<Int> = arrayListOf(),
    @SerializedName("category_names")
    val categoryNames: List<String> = arrayListOf(),
    @SerializedName("labels")
    val labels: List<String> = arrayListOf(),
    @SerializedName("delivery_services")
    val deliveryServices: List<DeliveryService> = arrayListOf(),
    @SerializedName("delivery_metadata")
    val deliveryMetadata: List<DeliveryMetadata> = arrayListOf(),
) : Parcelable
