package com.tokopedia.tokofood.purchase.promopage.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PromoListTokoFoodResponse(
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("status")
    @Expose
    val status: Int = 0,
    @SerializedName("data")
    @Expose
    val data: PromoListTokoFoodData = PromoListTokoFoodData()
)

class PromoListTokoFoodData(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("change_restriction_message")
    @Expose
    val changeRestrictionMessage: String = "",
    @SerializedName("error_page")
    @Expose
    val errorPage: PromoListTokoFoodErrorPage = PromoListTokoFoodErrorPage(),
    @SerializedName("empty_state")
    @Expose
    val emptyState: PromoListTokoFoodEmptyState = PromoListTokoFoodEmptyState(),
    @SerializedName("available_section")
    @Expose
    val availableSection: PromoListTokoFoodSection = PromoListTokoFoodSection(),
    @SerializedName("unavailable_section")
    @Expose
    val unavailableSection: PromoListTokoFoodSection = PromoListTokoFoodSection(),
    @SerializedName("promo_summary")
    @Expose
    val promoSummary: PromoListTokoFoodSummary = PromoListTokoFoodSummary()
)

data class PromoListTokoFoodErrorPage(
    @SerializedName("is_show_error_page")
    @Expose
    val isShowErrorPage: Boolean = false,
    @SerializedName("image")
    @Expose
    val image: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("button")
    @Expose
    val button: PromoListTokoFoodButton = PromoListTokoFoodButton()
)

data class PromoListTokoFoodButton(
    @SerializedName("text")
    @Expose
    val text: String = "",
    @SerializedName("color")
    @Expose
    val color: String = "",
    @SerializedName("action")
    @Expose
    val action: String = "",
    @SerializedName("link")
    @Expose
    val link: String = ""
)

data class PromoListTokoFoodEmptyState(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("image_url")
    @Expose
    val imageUrl: String = ""
)

data class PromoListTokoFoodSection(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("sub_title")
    @Expose
    val subtitle: String = "",
    @SerializedName("icon_url")
    @Expose
    val iconUrl: String = "",
    @SerializedName("is_enabled")
    @Expose
    val isEnabled: Boolean = false,
    @SerializedName("sub_sections")
    @Expose
    val subSection: PromoListTokoFoodSubSection = PromoListTokoFoodSubSection()
)

data class PromoListTokoFoodSubSection(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("icon_url")
    @Expose
    val iconUrl: String = "",
    @SerializedName("is_enabled")
    @Expose
    val isEnabled: Boolean = false,
    @SerializedName("ticker_message")
    @Expose
    val tickerMessage: String = "",
    @SerializedName("coupons")
    @Expose
    val coupons: List<PromoListTokoFoodCoupon> = listOf()
)

data class PromoListTokoFoodCoupon(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("expiry_info")
    @Expose
    val expiryInfo: String = "",
    @SerializedName("is_selected")
    @Expose
    val isSelected: Boolean = false,
    @SerializedName("top_banner_title")
    @Expose
    val topBannerTitle: String = "",
    @SerializedName("additional_information")
    @Expose
    val additionalInformation: String = ""
)

data class PromoListTokoFoodSummary(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("total")
    @Expose
    val total: Double = 0.0,
    @SerializedName("total_fmt")
    @Expose
    val totalFmt: String = ""
)