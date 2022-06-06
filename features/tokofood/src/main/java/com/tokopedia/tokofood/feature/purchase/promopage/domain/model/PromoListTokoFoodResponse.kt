package com.tokopedia.tokofood.feature.purchase.promopage.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil

class PromoListTokoFoodResponse(
    @SerializedName("promo_list_tokofood")
    @Expose
    val promoListTokoFood: PromoListTokoFood = PromoListTokoFood()
)

class PromoListTokoFood(
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("status")
    @Expose
    val status: String = "",
    @SerializedName("data")
    @Expose
    val data: PromoListTokoFoodData = PromoListTokoFoodData()
) {
    fun isSuccess(): Boolean = status == TokoFoodCartUtil.SUCCESS_STATUS
}

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
) {
    companion object {
        const val REFRESH_ACTION = "1"
        const val REDIRECT_ACTION = "2"
    }
}

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