package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class DeliveryProduct(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("id")
    val id: Long = 0L,
    @SerializedName("shipper_id")
    val shipperId: Long = 0L,
    @SerializedName("shipper_product_id")
    val shipperProductId: Long = 0L,
    @SerializedName("final_price")
    val finalPrice: Double = 0.0,
    @SerializedName("real_price")
    val realPrice: Double = 0.0,
    @SerializedName("text_final_price")
    val textFinalPrice: String = "",
    @SerializedName("text_real_price")
    val textRealPrice: String = "",
    @SerializedName("available")
    val available: Boolean = false,
    @SerializedName("hidden")
    val hidden: Boolean = false,
    @SerializedName("recommend")
    val recommend: Boolean = false,
    @SerializedName("text")
    val text: String = "",
    @SerializedName("promo_code")
    val promoCode: String = "",
) : Parcelable
