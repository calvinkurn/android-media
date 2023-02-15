package com.tokopedia.pdpsimulation.paylater.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.pdpsimulation.paylater.presentation.adapter.PayLaterAdapterFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PayLaterSimulationData(
    @SerializedName("paylater_getSimulationV3") val paylaterGetSimulation: PayLaterGetSimulation
) : Parcelable

@Parcelize
data class PayLaterGetSimulation(
    @SerializedName("data") val productList: List<PayLaterAllData>?
) : Parcelable

@Parcelize
data class PayLaterAllData(

    @SerializedName("tenure") val tenure: Int?,
    @SerializedName("text") val text: String?,
    @SerializedName("small_text") val smallText: String?,
    @SerializedName("sections") val detail: List<GatewaySection>
) : Parcelable

@Parcelize
data class GatewaySection(

    @SerializedName("title") val title: String?,
    @SerializedName("is_collapsible") val isCollapsible: Boolean?,
    @SerializedName("detail") val detail: List<Detail>
) : Parcelable

@Parcelize
data class Cta(

    @SerializedName("name") val name: String?,
    @SerializedName("android_url") var android_url: String? = "",
    @SerializedName("cta_type") val cta_type: Int?,
    @SerializedName("button_color") val button_color: String?,
    @SerializedName("bottom_sheet") val bottomSheet: BottomSheetDetail?
) : Parcelable

@Parcelize
data class BottomSheetDetail(

    @SerializedName("show") val isShow: Boolean?,
    @SerializedName("image") val imageUrl: String?,
    @SerializedName("title") val bottomSheetTitle: String?,
    @SerializedName("description") val bottomSheetDescription: String?,
    @SerializedName("button_text") val bottomSheetButtonText: String?,
) : Parcelable

@Parcelize
data class Detail(

    @SerializedName("gateway_detail") val gatewayDetail: GatewayModel?,
    @SerializedName("installment_per_month") val installment_per_month: Double?,
    @SerializedName("installment_per_month_ceil") val installment_per_month_ceil: Int?,
    @SerializedName("tenure") val tenure: Int?,
    @SerializedName("subheader") val subheader: String?,
    @SerializedName("recommended") val recommendationDetail: RecommendationDetail?,
    @SerializedName("benefits") val benefits: List<String>?,
    @SerializedName("disable") val paylaterDisableDetail: DisableDetail?,
    @SerializedName("ticker") val ticker: Ticker = Ticker(),
    @SerializedName("cta") val cta: Cta,
    @SerializedName("installment_details") val installementDetails: InstallmentDetails?,
    @SerializedName("user_state") val userState: String?,
    @SerializedName("user_balance_amt") val limit: String?,
    @SerializedName("tenure_header") val optionalTenureHeader: String?,
    @SerializedName("linking_status") val linkingStatus: String?
) : BasePayLaterWidgetUiModel, Parcelable {
    override fun type(typeFactory: PayLaterAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

@Parcelize
data class RecommendationDetail(

    @SerializedName("flag") val flag: Boolean?,
    @SerializedName("text") val text: String?,
    @SerializedName("color") val color: String?,
) : Parcelable

@Parcelize
data class DisableDetail(

    @SerializedName("status") val status: Boolean?,
) : Parcelable

@Parcelize
data class Ticker(
    @SerializedName("is_shown") val isShown: Boolean = false,
    @SerializedName("type") val type: String = "",
    @SerializedName("content") val content: String = "",
) : Parcelable

@Parcelize
data class GatewayModel(

    @SerializedName("id") val gateway_id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("code") val gatewayCode: String?,
    @SerializedName("img_light_url") val img_light_url: String?,
    @SerializedName("img_dark_url") val img_dark_url: String?,
    @SerializedName("how_to_use") val howToUse: HowToUse?,
    @SerializedName("payment_gateway_code") val paymentGatewayCode: String?

) : Parcelable

@Parcelize
data class InstallmentDetails(

    @SerializedName("header") val header: String?,
    @SerializedName("content") val content: List<Content>?,
) : Parcelable

@Parcelize
data class Content(

    @SerializedName("title") val title: String?,
    @SerializedName("value") val value: String?,
    @SerializedName("type") val type: Int?,
) : BasePayLaterWidgetUiModel, Parcelable {
    override fun type(typeFactory: PayLaterAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

@Parcelize
data class HowToUse(

    @SerializedName("notes") val notes: List<String>?,
    @SerializedName("steps") val steps: List<String>?
) : Parcelable
