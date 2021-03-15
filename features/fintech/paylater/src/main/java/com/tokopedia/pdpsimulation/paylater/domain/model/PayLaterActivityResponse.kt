package com.tokopedia.pdpsimulation.paylater.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class PayLaterActivityResponse(
        @SerializedName("paylater_getActiveProduct")
        val productData: PayLaterProductData = PayLaterProductData(),
)

@Parcelize
data class PayLaterProductData(
        @SerializedName("product")
        val productList: ArrayList<PayLaterItemProductData>? = arrayListOf(),
) : Parcelable

@Parcelize
data class PayLaterItemProductData(
        @SerializedName("id")
        val productId: Long?,
        @SerializedName("name")
        val partnerName: String?,
        @SerializedName("subheader")
        val subHeader: String?,
        @SerializedName("gateway_code")
        val gateWayCode: String?,
        @SerializedName("img_light_url")
        val partnerImgLightUrl: String?,
        @SerializedName("img_dark_url")
        val partnerImgDarkUrl: String?,
        @SerializedName("benefit")
        val partnerBenefitList: ArrayList<PayLaterPartnerBenefit>?,
        @SerializedName("faq")
        val partnerFaqList: ArrayList<PayLaterPartnerFaq>?,
        @SerializedName("faq_url")
        val partnerFaqUrl: String?,
        @SerializedName("how_to_use")
        val partnerUsageDetails: PayLaterPartnerStepDetails?,
        @SerializedName("how_to_apply")
        val partnerApplyDetails: PayLaterPartnerStepDetails?,
        @SerializedName("is_able_apply")
        val isAbleToApply: Boolean?,
        @SerializedName("how_to_action_url")
        val actionWebUrl: String?,
) : Parcelable

@Parcelize
data class PayLaterPartnerBenefit(
        @SerializedName("content")
        val partnerBenefitContent: String?,
        @SerializedName("is_highlight")
        val isHighlighted: Boolean?,
) : Parcelable

@Parcelize
data class PayLaterPartnerFaq(
        @SerializedName("question")
        val faqQuestion: String?,
        @SerializedName("answer")
        val faqAnswer: String?,
        var expandLayout: Boolean = false,
) : Parcelable

@Parcelize
data class PayLaterPartnerStepDetails(
        @SerializedName("notes")
        val partnerNotes: ArrayList<String>?,
        @SerializedName("steps")
        val partnerSteps: ArrayList<String>?,
) : Parcelable





