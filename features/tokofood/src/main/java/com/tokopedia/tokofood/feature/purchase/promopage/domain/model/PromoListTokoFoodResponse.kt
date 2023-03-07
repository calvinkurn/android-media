package com.tokopedia.tokofood.feature.purchase.promopage.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.data.model.response.Header
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil

data class PromoListTokoFoodResponse(
    @SerializedName("cart_general_promo_list")
    val cartGeneralPromoList: CartGeneralPromoList = CartGeneralPromoList()
)

data class CartGeneralPromoList(
    @SerializedName("header")
    val header: Header = Header(),
    @SerializedName("data")
    val data: CartGeneralPromoListData = CartGeneralPromoListData()
)

data class CartGeneralPromoListData(
    @SerializedName("success")
    val success: Int = Int.ZERO,
    @SerializedName("message")
    val message: String = String.EMPTY,
    @SerializedName("data")
    val data: CartGeneralPromoListDataData = CartGeneralPromoListDataData()
) {
    fun isSuccess(): Boolean = success == TokoFoodCartUtil.SUCCESS_STATUS_INT
}

data class CartGeneralPromoListDataData(
    @SerializedName("business_data")
    val businessData: List<CartGeneralPromoListBusinessData> = listOf()
) {
    fun getTokofoodBusinessData(): CartGeneralPromoListBusinessData {
        return businessData.firstOrNull { it.businessId == TokoFoodCartUtil.getBusinessId() } ?: CartGeneralPromoListBusinessData()
    }
}

data class CartGeneralPromoListBusinessData(
    @SerializedName("business_id")
    val businessId: String = String.EMPTY,
    @SerializedName("success")
    val success: Int = Int.ZERO,
    @SerializedName("message")
    val message: String = String.EMPTY,
    @SerializedName("custom_response")
    val nullableCustomResponse: CartGeneralBusinessDataCustomResponse? = CartGeneralBusinessDataCustomResponse()
) {

    val customResponse: CartGeneralBusinessDataCustomResponse
        get() = nullableCustomResponse ?: CartGeneralBusinessDataCustomResponse()

}

data class CartGeneralBusinessDataCustomResponse(
    @SerializedName("title")
    val title: String = String.EMPTY,
    @SerializedName("change_restriction_message")
    val changeRestrictionMessage: String = String.EMPTY,
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
