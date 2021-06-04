package com.tokopedia.promocheckoutmarketplace.data.response

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
        @SerializedName("additional_message")
        val additionalMessage: String = "",
        @SerializedName("reward_points_info")
        val rewardPointsInfo: RewardPointsInfo = RewardPointsInfo()
)

data class RewardPointsInfo(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("gain_reward_points_tnc")
        val gainRewardPointsTnc: GainRewardPointsTnc = GainRewardPointsTnc()
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
        @SerializedName("title")
        val title: String = "",
        @SerializedName("sub_title")
        val subTitle: String = "",
        @SerializedName("icon_url")
        val iconUrl: String = "",
        @SerializedName("is_enabled")
        val isEnabled: Boolean = false,
        @SerializedName("is_collapsed")
        val isCollapsed: Boolean = false,
        @SerializedName("tags")
        val tags: List<String> = emptyList(),
        @SerializedName("sub_sections")
        val subSections: List<SubSection> = emptyList()
)

data class SubSection(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("sub_title")
        val subTitle: String = "",
        @SerializedName("icon_url")
        val iconUrl: String = "",
        @SerializedName("is_enabled")
        val isEnabled: Boolean = false,
        @SerializedName("is_collapsed")
        val isCollapsed: Boolean = false,
        @SerializedName("tags")
        val tags: List<String> = emptyList(),
        @SerializedName("coupons")
        val coupons: List<Coupon> = emptyList()
)

data class Coupon(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("expiry_info")
        val expiryInfo: String = "",
        @SerializedName("expiry_count_down")
        val expiryCountDown: Int = 0,
        @SerializedName("coupon_url")
        val couponUrl: String = "",
        @SerializedName("coupon_app_link")
        val couponAppLink: String = "",
        @SerializedName("unique_id")
        val uniqueId: String = "",
        @SerializedName("shop_id")
        val shopId: Int = 0,
        @SerializedName("tag_image_urls")
        val tagImageUrls: List<String> = emptyList(),
        @SerializedName("benefit_amount")
        val benefitAmount: Int = 0,
        @SerializedName("is_recommended")
        val isRecommended: Boolean = false,
        @SerializedName("is_selected")
        var isSelected: Boolean = false,
        @SerializedName("is_attempted")
        val isAttempted: Boolean = false,
        @SerializedName("radio_check_state")
        val radioCheckState: String = "",
        @SerializedName("clashing_infos")
        val clashingInfos: List<ClashingInfo> = emptyList(),
        @SerializedName("currency_details_str")
        val currencyDetailStr: String = "",
        @SerializedName("coachmark")
        val coachMark: PromoCoachmark = PromoCoachmark()
)

data class ClashingInfo(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("message")
        val message: String = ""
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