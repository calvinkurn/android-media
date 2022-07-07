package com.tokopedia.tokofood.feature.purchase.promopage.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil

class PromoListTokoFoodResponse(
    @SerializedName("promo_list_tokofood")
    val promoListTokoFood: PromoListTokoFood = PromoListTokoFood()
)

class PromoListTokoFood(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("data")
    val data: PromoListTokoFoodData = PromoListTokoFoodData()
) {
    fun isSuccess(): Boolean = status == TokoFoodCartUtil.SUCCESS_STATUS
}

class PromoListTokoFoodData(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("change_restriction_message")
    val changeRestrictionMessage: String = "",
    @SerializedName("error_page")
    val errorPage: PromoListTokoFoodErrorPage = PromoListTokoFoodErrorPage(),
    @SerializedName("empty_state")
    val emptyState: PromoListTokoFoodEmptyState = PromoListTokoFoodEmptyState(),
    @SerializedName("available_section")
    val availableSection: PromoListTokoFoodSection = PromoListTokoFoodSection(),
    @SerializedName("unavailable_section")
    val unavailableSection: PromoListTokoFoodSection = PromoListTokoFoodSection(),
    @SerializedName("promo_summary")
    val promoSummary: PromoListTokoFoodSummary = PromoListTokoFoodSummary()
)

data class PromoListTokoFoodErrorPage(
    @SerializedName("is_show_error_page")
    val isShowErrorPage: Boolean = false,
    @SerializedName("image")
    val image: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("button")
    val button: List<PromoListTokoFoodButton> = listOf()
)

data class PromoListTokoFoodButton(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("color")
    val color: String = "",
    @SerializedName("action")
    val action: String = "",
    @SerializedName("link")
    val link: String = ""
) {
    companion object {
        const val REFRESH_ACTION = "1"
        const val REDIRECT_ACTION = "2"
    }
}

data class PromoListTokoFoodEmptyState(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("image_url")
    val imageUrl: String = ""
)

data class PromoListTokoFoodSection(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("sub_title")
    val subtitle: String = "",
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @SerializedName("is_enabled")
    val isEnabled: Boolean = false,
    @SerializedName("sub_section")
    val subSection: PromoListTokoFoodSubSection = PromoListTokoFoodSubSection()
)

data class PromoListTokoFoodSubSection(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @SerializedName("is_enabled")
    val isEnabled: Boolean = false,
    @SerializedName("ticker_message")
    val tickerMessage: String = "",
    @SerializedName("coupons")
    val coupons: List<PromoListTokoFoodCoupon> = listOf()
)

data class PromoListTokoFoodCoupon(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("expiry_info")
    val expiryInfo: String = "",
    @SerializedName("is_selected")
    val isSelected: Boolean = false,
    @SerializedName("top_banner_title")
    val topBannerTitle: String = "",
    @SerializedName("additional_information")
    val additionalInformation: String = ""
)

data class PromoListTokoFoodSummary(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("total")
    val total: Double = 0.0,
    @SerializedName("total_fmt")
    val totalFmt: String = ""
)