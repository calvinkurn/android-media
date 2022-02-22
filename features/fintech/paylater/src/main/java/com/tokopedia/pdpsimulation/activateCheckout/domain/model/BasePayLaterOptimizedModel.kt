package com.tokopedia.pdpsimulation.activateCheckout.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BasePayLaterOptimizedModel(
    @SerializedName("paylater_getOptimizedCheckout") val paylatergetOptimizedCheckout: PaylaterGetOptimizedModel

) : Parcelable

@Parcelize
data class PaylaterGetOptimizedModel(
    @SerializedName("data") val checkoutData: List<CheckoutData>,
    @SerializedName("footer") val footer: String
) : Parcelable

@Parcelize
data class CheckoutData(
    @SerializedName("gateway_id") val gateway_id: Int,
    @SerializedName("gateway_code") val gateway_code: String,
    @SerializedName("payment_gateway_code") val paymentGatewayCode:String?,
    @SerializedName("gateway_name") val gateway_name: String,
    @SerializedName("subtitle") val subtitle: String,
    @SerializedName("subtitle2") val subtitle2: String,
    @SerializedName("light_img_url") val light_img_url: String,
    @SerializedName("dark_img_url") val dark_img_url: String,
    @SerializedName("disable") val disable: Boolean,
    @SerializedName("reason_long") val reason_long: String,
    @SerializedName("reason_short") val reason_short: String,
    @SerializedName("detail") val tenureDetail: List<TenureDetail>,
    @SerializedName("user_state") val userState: String? = null,
    @SerializedName("user_balance_amt") val userAmount: String? = null,
    var selectedGateway: Boolean = false,

    ) : Parcelable

@Parcelize
data class TenureDetail(
    var isSelectedTenure: Boolean = false,
    @SerializedName("tenure") val tenure: Int,
    @SerializedName("label") val lable: String?,
    @SerializedName("chip_title") val chip_title: String,
    @SerializedName("monthly_installment") val monthly_installment: String,
    @SerializedName("description") val description: String,
    @SerializedName("installment_details") val installment_details: ActivationInstallmentDetails
) : Parcelable

@Parcelize
data class ActivationInstallmentDetails(
    @SerializedName("header") val header: String,
    @SerializedName("content") val detailContent: List<DetailContent>
) : Parcelable

@Parcelize
data class DetailContent(
    @SerializedName("title") val title: String,
    @SerializedName("value") val value: String,
    @SerializedName("type") val type: Int
) : Parcelable


