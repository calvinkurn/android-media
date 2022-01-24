package com.tokopedia.vouchercreation.product.create.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CouponPreviewRequestParams(
    @SerializedName("platform")
    @Expose
    val platform : String,

    @SerializedName("is_public")
    @Expose
    val  isPublic: String,

    @SerializedName("voucher_benefit_type")
    @Expose
    val voucherBenefitType: String,

    @SerializedName("voucher_cashback_type")
    @Expose
    val voucherCashbackType: String,

    @SerializedName("voucher_cashback_percentage")
    @Expose
    val voucherCashbackPercentage: Int,

    @SerializedName("voucher_nominal_amount")
    @Expose
    val voucherNominalAmount: Int,

    @SerializedName("voucher_nominal_symbol")
    @Expose
    val voucherNominalSymbol: String,

    @SerializedName("shop_logo")
    @Expose
    val shopLogo: String,

    @SerializedName("shop_name")
    @Expose
    val shopName: String,

    @SerializedName("voucher_code")
    @Expose
    val voucherCode: String,

    @SerializedName("voucher_start_time")
    @Expose
    val voucherStartTime: String,

    @SerializedName("voucher_finish_time")
    @Expose
    val voucherFinishTime: String,

    @SerializedName("product_count")
    @Expose
    val productCount: Int,

    @SerializedName("product_image_1")
    @Expose
    val productImage1: String,

    @SerializedName("audience_target")
    @Expose
    val audienceTarget: String
)
