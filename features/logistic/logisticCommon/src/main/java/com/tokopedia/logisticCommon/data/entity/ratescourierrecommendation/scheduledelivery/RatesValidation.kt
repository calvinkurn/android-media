package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class RatesValidation(
    @SerializedName("error")
    val error: Int = 0,
    @SerializedName("text")
    val text: String = "",
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("promo_code")
    val promoCode: String = "",
    @SerializedName("extra_data")
    val extraData: String = "",
    @SerializedName("payment_expire_time")
    val paymentExpireTime: Int = 0,
    @SerializedName("schedule_metadata")
    val scheduleMetadata: String = "",
) : Parcelable
