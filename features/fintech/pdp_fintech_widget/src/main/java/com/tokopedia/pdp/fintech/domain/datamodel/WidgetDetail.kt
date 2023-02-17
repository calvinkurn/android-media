package com.tokopedia.pdp.fintech.domain.datamodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class WidgetDetail(
    @SerializedName("paylater_getPDPWidgetV2") var baseWidgetResponse: BaseDataResponse? = null
)


@Parcelize
data class BaseDataResponse(
    @SerializedName("data") var baseData: BaseChipResponse? = null
) : Parcelable

@Parcelize
data class BaseChipResponse(
    @SerializedName("list") var list: ArrayList<ChipList> = arrayListOf()
) : Parcelable


@Parcelize
data class ChipList(

    @SerializedName("price") var price: Double? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("chips") var chips: ArrayList<ChipsData> = arrayListOf()

) : Parcelable


@Parcelize
data class ChipsData(
    @SerializedName("gateway_id") var gatewayId: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("product_code") var productCode: String? = null,
    @SerializedName("is_active") var isActive: Boolean? = null,
    @SerializedName("is_disabled") var isDisabled: Boolean? = null,
    @SerializedName("tenure") var tenure: Int? = null,
    @SerializedName("header") var header: String? = null,
    @SerializedName("subheader") var subheader: String? = null,
    @SerializedName("linking_status") var linkingStatus: String? = null,
    @SerializedName("subheader_color") var subheaderColor: String? = null,
    @SerializedName("product_icon_light") var productIconLight: String? = null,
    @SerializedName("product_icon_dark") var productIconDark: String? = null,
    @SerializedName("user_state") var userStatus: String? = null,
    @SerializedName("user_balance_amt") var userBalanceAmount: String? = null,
    @SerializedName("installment_amt") var installmentAmount: String? = null,
    @SerializedName("cta") var cta: Cta? = Cta(),
    @SerializedName("promo_name") val promoName: String? = null,
) : Parcelable

@Parcelize
data class Cta(
    @SerializedName("type") var type: Int? = null,
    @SerializedName("web_url") var webUrl: String? = null,
    @SerializedName("android_url") var androidUrl: String? = null,
    @SerializedName("ios_url") var iosUrl: String? = null,
    @SerializedName("bottomsheet") var bottomsheet: WidgetBottomsheet? = WidgetBottomsheet()
) : Parcelable

@Parcelize
data class WidgetBottomsheet(
    @SerializedName("show") var show: Boolean? = null,
    @SerializedName("product_icon_light") var productIconLight: String? = null,
    @SerializedName("product_icon_dark") var productIconDark: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("buttons") var buttons: ArrayList<ActivationBottomSheetButtons> = arrayListOf(),
    @SerializedName("descriptions") var descriptions: ArrayList<ActivationBottomSheetDescriptions> = arrayListOf(),
    @SerializedName("product_footnote") var productFootnote: String? = null,
    @SerializedName("product_footnote_icon_light") var productFootnoteIconLight: String? = null,
    @SerializedName("product_footnote_icon_dark") var productFootnoteIconDark: String? = null,
    @SerializedName("footnote") var footnote: String? = null,
    @SerializedName("footnote_icon_light") var footnoteIconLight: String? = null,
    @SerializedName("footnote_icon_dark") var footnoteIconDark: String? = null
) : Parcelable

@Parcelize
data class ActivationBottomSheetButtons(
    @SerializedName("button_text") var buttonText: String? = null,
    @SerializedName("button_text_color") var buttonTextColor: String? = null,
    @SerializedName("button_color") var buttonColor: String? = null,
    @SerializedName("button_url") var buttonUrl: String? = null
) : Parcelable

@Parcelize
data class ActivationBottomSheetDescriptions(
    @SerializedName("line_icon_dark") var lineIconDark: String? = null,
    @SerializedName("line_icon_light") var lineIconLight: String? = null,
    @SerializedName("text") var text: String? = null
) : Parcelable
