package com.tokopedia.pdpsimulation.activateCheckout.domain.model

import com.google.gson.annotations.SerializedName

data class BasePayLaterOptimizedModel(
    @SerializedName("paylater_getOptimizedCheckout") val paylatergetOptimizedCheckout : PaylaterGetOptimizedModel

)

data class PaylaterGetOptimizedModel(
    @SerializedName("data") val checkoutData : List<CheckoutData>,
    @SerializedName("footer") val footer : String
)

data class CheckoutData (
    @SerializedName("gateway_id") val gateway_id : Int,
    @SerializedName("gateway_code") val gateway_code : String,
    @SerializedName("gateway_name") val gateway_name : String,
    @SerializedName("subtitle") val subtitle : String,
    @SerializedName("subtitle2") val subtitle2 : String,
    @SerializedName("light_img_url") val light_img_url : String,
    @SerializedName("dark_img_url") val dark_img_url : String,
    @SerializedName("disable") val disable : Boolean,
    @SerializedName("reason_long") val reason_long : String,
    @SerializedName("reason_short") val reason_short : String,
    @SerializedName("detail") val tenureDetail : List<TenureDetail>
        )

data class TenureDetail (
    @SerializedName("tenure") val tenure : Int,
    @SerializedName("chip_title") val chip_title : String,
    @SerializedName("monthly_installment") val monthly_installment : String,
    @SerializedName("description") val description : String,
    @SerializedName("installment_details") val installment_details : ActivationInstallmentDetails
)

data class ActivationInstallmentDetails (
    @SerializedName("header") val header : String,
    @SerializedName("content") val detailContent : List<DetailContent>
        )

data class DetailContent (
    @SerializedName("title") val title : String,
    @SerializedName("value") val value : String,
    @SerializedName("type") val type : Int
        )


