package com.tokopedia.promocheckoutmarketplace.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CouponListRecommendationResponse(
        @SerializedName("coupon_list_recommendation")
        val couponListRecommendation: CouponListRecommendation = CouponListRecommendation()
)

data class CouponListRecommendation(
        @SerializedName("message")
        val message: List<String> = emptyList(),
        @SerializedName("error_code")
        val errorCode: String = "",
        @SerializedName("status")
        var status: String = "",
        @SerializedName("data")
        val data: Data = Data()
)

data class Data(
        @SerializedName("error_page")
        val errorPage: ErrorPage = ErrorPage(),
        @SerializedName("result_status")
        val resultStatus: ResultStatus = ResultStatus(),
        @SerializedName("empty_state")
        val emptyState: EmptyStateResponse = EmptyStateResponse(),
        @SerializedName("title")
        val title: String = "",
        @SerializedName("sub_title")
        val subTitle: String = "",
        @SerializedName("promo_recommendation")
        val promoRecommendation: PromoRecommendation = PromoRecommendation(),
        @SerializedName("coupon_sections")
        val couponSections: List<CouponSection> = emptyList(),
        @SerializedName("attempted_promo_code_error")
        val attemptedPromoCodeError: AttemptedPromoCodeError = AttemptedPromoCodeError(),
        @SerializedName("section_tabs")
        val sectionTabs: List<SectionTab> = emptyList(),
        @SerializedName("bottom_sheet")
        val bottomSheet: BottomSheet = BottomSheet()
)

data class GainRewardPointsTnc(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("tnc_details")
        val tncDetails: List<TncDetail> = emptyList()
)

data class TncDetail(
        @SerializedName("icon_image_url")
        val iconImageUrl: String = "",
        @SerializedName("description")
        val description: String = ""
)

data class ErrorPage(
        @SerializedName("is_show_error_page")
        val isShowErrorPage: Boolean = false,
        @SerializedName("image")
        val img: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val desc: String = "",
        @SerializedName("button")
        val button: Button = Button()
)

data class Button(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("destination")
        val destination: String = ""
)

data class ResultStatus(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("message")
        val message: List<String> = emptyList(),
        @SerializedName("reason")
        val reason: String = ""
) {
    companion object {
        const val STATUS_COUPON_LIST_EMPTY = "42050"
        const val STATUS_PHONE_NOT_VERIFIED = "42049"
        const val STATUS_USER_BLACKLISTED = "42003"
    }
}

data class EmptyStateResponse(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("image_url")
        val imageUrl: String = ""
)

data class PromoRecommendation(
        @SerializedName("codes")
        val codes: List<String> = emptyList(),
        @SerializedName("message")
        val message: String = ""
)

data class CouponSection(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("sub_title")
        val subTitle: String = "",
        @SerializedName("is_enabled")
        val isEnabled: Boolean = false,
        @SerializedName("sub_sections")
        val subSections: List<SubSection> = emptyList()
)

data class CouponGroup(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("count")
        val count: Int = 0
)

data class SubSection(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("sub_title")
        val subTitle: String = "",
        @SerializedName("icon_unify")
        val iconUnify: String = "",
        @SerializedName("icon_url")
        val iconUrl: String = "",
        @SerializedName("is_enabled")
        val isEnabled: Boolean = false,
        @SerializedName("coupons")
        val coupons: List<Coupon> = emptyList(),
        @SerializedName("coupon_groups")
        val couponGroups: List<CouponGroup> = emptyList()
) {
    companion object {
        const val ICON_COUPON = "COUPON"
        const val ICON_BADGE_OS_FILLED = "BADGE_OS_FILLED"
        const val ICON_BADGE_PMPRO_FILLED = "BADGE_PMPRO_FILLED"
        const val ICON_BADGE_PM_FILLED = "BADGE_PM_FILLED"
        const val ICON_SHOP_FILLED = "SHOP_FILLED"
        const val ICON_BADGE_NOW_FILLED = "BADGE_NOW_FILLED"
    }
}

data class Coupon(
        @SerializedName("promo_id")
        val promoId: String = "",
        @SerializedName("code")
        val code: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("coupon_app_link")
        val couponAppLink: String = "",
        @SerializedName("unique_id")
        val uniqueId: String = "",
        @SerializedName("shop_id")
        val shopId: String = "",
        @SerializedName("benefit_amount")
        val benefitAmount: Int = 0,
        @SerializedName("is_recommended")
        val isRecommended: Boolean = false,
        @SerializedName("is_selected")
        var isSelected: Boolean = false,
        @SerializedName("is_attempted")
        val isAttempted: Boolean = false,
        @SerializedName("clashing_infos")
        val clashingInfos: List<ClashingInfo> = emptyList(),
        @SerializedName("bo_clashing_infos")
        val boClashingInfos: List<BoClashingInfo> = emptyList(),
        @SerializedName("currency_details_str")
        val currencyDetailStr: String = "",
        @SerializedName("coachmark")
        val coachMark: PromoCoachmark = PromoCoachmark(),
        @SerializedName("is_highlighted")
        val isHighlighted: Boolean = false,
        @SerializedName("group_id")
        val groupId: String = "",
        @SerializedName("is_group_header")
        val isGroupHeader: Boolean = false,
        @SerializedName("promo_infos")
        val promoInfos: List<PromoInfo> = emptyList(),
        @SerializedName("benefit_details")
        val benefitDetail: List<BenefitDetail> = emptyList(),
        @SerializedName("is_bebas_ongkir")
        val isBebasOngkir: Boolean = false,
        @SerializedName("additional_bo_datas")
        val additionalBoData: List<AdditionalBoData> = emptyList(),
)

data class AdditionalBoData(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("unique_id")
        val uniqueId: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("shipping_id")
        val shippingId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("sp_id")
        val shipperProductId: Int = 0,
        @SerializedName("benefit_amount")
        val benefitAmount: Double = 0.0,
        @SerializedName("promo_id")
        val promoId: Long = 0,
        @SerializedName("shipping_price")
        val shippingPrice: Double = 0.0,
        @SerializedName("shipping_subsidy")
        val shippingSubsidy: Long = 0,
        @SerializedName("benefit_class")
        val benefitClass: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("bo_campaign_id")
        val boCampaignId: Long = 0,
        @SerializedName("eta_txt")
        val etaText: String = "",
)

data class BenefitDetail(
        @SerializedName("amount_idr")
        val amountIdr: Double = 0.0,
        @SerializedName("benefit_type")
        val benefitType: String = "",
        @SerializedName("data_type")
        val dataType: String = ""
)

data class PromoInfo(
        @SerializedName("type")
        val type: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("validation_type")
        val validationType: String = "",
        @SerializedName("methods")
        val methods: List<String> = emptyList()
) {
    companion object {
        const val VALIDATION_TYPE_SHIPPING = "shipping"
        const val VALIDATION_TYPE_PAYMENT = "payment"

        const val TYPE_TOP_BANNER = "top_banner"
        const val TYPE_PROMO_INFO = "promo_info"
        const val TYPE_BOTTOM_BANNER = "bottom_banner"
        const val TYPE_PROMO_VALIDITY = "promo_validity"

        const val ICON_USER = "USER"
        const val ICON_INFORMATION = "INFORMATION"
        const val ICON_FINANCE = "FINANCE"
        const val ICON_CLOCK = "CLOCK"
        const val ICON_COURIER = "COURIER"
        const val ICON_TOKO_MEMBER = "TOKOMEMBER"
        const val ICON_DOT = "DOT"
        const val ICON_NONE = "NONE"
        const val ICON_URL = "URL"
    }
}

data class ClashingInfo(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("icon")
        val icon: String = ""
)

data class BoClashingInfo(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("icon")
        val icon: String = ""
)

data class PromoCoachmark(
        @SerializedName("is_shown")
        @Expose
        val isShown: Boolean = false,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("content")
        @Expose
        val content: String = ""
)

data class AttemptedPromoCodeError(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("message")
        val message: String = ""
)

data class SectionTab(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("title")
        val title: String = ""
)

data class BottomSheet(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("content_title")
        val contentTitle: String = "",
        @SerializedName("content_description")
        val contentDescription: String = "",
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("button_txt")
        val buttonText: String = ""
)