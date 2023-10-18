package com.tokopedia.mvc.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateVoucherRequest(
    @SerializedName("voucher_id")
    @Expose
    val voucherId: Long = 0,
    @SerializedName("benefit_idr")
    @Expose
    val benefitIdr: Int = 0,
    @SerializedName("benefit_max")
    @Expose
    val benefitMax: Int = 0,
    @SerializedName("benefit_percent")
    @Expose
    val benefitPercent: Int = 0,
    @SerializedName("benefit_type")
    @Expose
    val benefitType: String = "",
    @SerializedName("code")
    @Expose
    val code: String = "",
    @SerializedName("coupon_name")
    @Expose
    val couponName: String = "",
    @SerializedName("coupon_type")
    @Expose
    val couponType: String = "",
    @SerializedName("date_end")
    @Expose
    val dateEnd: String = "",
    @SerializedName("date_start")
    @Expose
    val dateStart: String = "",
    @SerializedName("hour_end")
    @Expose
    val hourEnd: String = "",
    @SerializedName("hour_start")
    @Expose
    val hourStart: String = "",
    @SerializedName("image")
    @Expose
    val image: String = "",
    @SerializedName("image_square")
    @Expose
    val imageSquare: String = "",
    @SerializedName("image_portrait")
    @Expose
    val imagePortrait: String = "",
    @SerializedName("is_public")
    @Expose
    val isPublic: Int = 0,
    @SerializedName("min_purchase")
    @Expose
    val minPurchase: Int = 0,
    @SerializedName("quota")
    @Expose
    val quota: Int = 0,
    @SerializedName("token")
    @Expose
    val token: String = "",
    @SerializedName("source")
    @Expose
    val source: String = ""
)
