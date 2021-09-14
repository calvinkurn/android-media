package com.tokopedia.pdpsimulation.paylater.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PayLaterSimulationData(

    @SerializedName("paylater_getSimulationV2") val paylaterGetSimulation: PayLaterGetSimulation
) : Parcelable


@Parcelize
data class PayLaterGetSimulation(

    @SerializedName("data") val productList: List<PayLaterAllData>?
) : Parcelable

@Parcelize
data class PayLaterAllData(
    @SerializedName("tenure") val tenure: Int?,
    @SerializedName("text") val text: String?,
    @SerializedName("detail") val detail: List<Detail>?
) : Parcelable

@Parcelize
data class Benefit(

    @SerializedName("content") val content: String?,
    @SerializedName("is_highlight") val is_highlight: Boolean?
) : Parcelable

@Parcelize
data class Cta(

    @SerializedName("name") val name: String?,
    @SerializedName("description_1") val description_1: String?,
    @SerializedName("description_2") val description_2: String?,
    @SerializedName("web_url") val web_url: String?,
    @SerializedName("ios_url") val ios_url: String?,
    @SerializedName("android_url") val android_url: String?,
    @SerializedName("cta_type") val cta_type: Int?,
    @SerializedName("is_redirect_url") val is_redirect_url: Int?,
    @SerializedName("button_color") val button_color: String?
) : Parcelable

@Parcelize
data class Detail(

    @SerializedName("gateway_id") val gateway_id: Int?,
    @SerializedName("installment_per_month") val installment_per_month: Int?,
    @SerializedName("installment_per_month_ceil") val installment_per_month_ceil: Double?,
    @SerializedName("total_fee") val total_fee: Int?,
    @SerializedName("total_fee_ceil") val total_fee_ceil: Double?,
    @SerializedName("total_interest") val total_interest: Int?,
    @SerializedName("total_interest_ceil") val total_interest_ceil: Double?,
    @SerializedName("interest_pct") val interest_pct: Double?,
    @SerializedName("total_with_provision") val total_with_provision: Int?,
    @SerializedName("total_with_provision_ceil") val total_with_provision_ceil: Int?,
    @SerializedName("is_recommended") val is_recommended: Boolean?,
    @SerializedName("tenure") val tenure: Int?,
    @SerializedName("activation_status") val activation_status: Int?,
    @SerializedName("account_status") val account_status: Int?,
    @SerializedName("disable") val disableDetail: DisableDetail?,
    @SerializedName("cta") val cta: Cta?,
    @SerializedName("gateway_detail") val gateway_detail: GatewayDetail?,
    @SerializedName("service_fee_info") val serviceFeeInfo: String?
) : Parcelable

@Parcelize
data class DisableDetail(
    @SerializedName("status") val status: Boolean?,
    @SerializedName("header") val header: String?,
    @SerializedName("description") val description: String?
) : Parcelable

@Parcelize
data class Faq(

    @SerializedName("question") val question: String?,
    @SerializedName("answer") val answer: String?,
    var expandLayout: Boolean = false
) : Parcelable

@Parcelize
data class GatewayDetail(

    @SerializedName("gateway_id") val gateway_id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("product_code") val product_code: String?,
    @SerializedName("is_active") val is_active: Boolean?,
    @SerializedName("small_subheader") val smallSubHeader: String?,
    @SerializedName("subheader") val subheader: String?,
    @SerializedName("img_light_url") val img_light_url: String?,
    @SerializedName("img_dark_url") val img_dark_url: String?,
    @SerializedName("faq_url") val faq_url: String?,
    @SerializedName("apply_url") val apply_url: String?,
    @SerializedName("benefit") val benefit: List<Benefit?>?,
    @SerializedName("detail") val detail: List<SingleProductDetail?>?,
    @SerializedName("faq") val faq: List<Faq?>?,
    @SerializedName("how_to_use") val how_toUse: HowToUse?,
    @SerializedName("how_to_apply") val how_toApply: HowToApply?,
    @SerializedName("tnc") val tnc: List<String?>?
) : Parcelable

@Parcelize
data class SingleProductDetail(
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: List<String>?
) : Parcelable


@Parcelize
data class HowToApply(

    @SerializedName("notes") val notes: List<String?>?,
    @SerializedName("steps") val steps: List<String?>?
) : Parcelable

@Parcelize
data class HowToUse(

    @SerializedName("notes") val notes: List<String>?,
    @SerializedName("steps") val steps: List<String?>?
) : Parcelable

