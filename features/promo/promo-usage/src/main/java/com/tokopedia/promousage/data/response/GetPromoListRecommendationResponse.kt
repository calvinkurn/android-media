package com.tokopedia.promousage.data.response

import com.google.gson.annotations.SerializedName

data class GetPromoListRecommendationResponse(
    @SerializedName("GetPromoListRecommendation")
    val promoListRecommendation: GetPromoListRecommendationResponseData = GetPromoListRecommendationResponseData()
)

data class GetPromoListRecommendationEntryPointResponse(
    @SerializedName("GetPromoListRecommendation")
    val promoListRecommendation: GetPromoListRecommendationResponseData = GetPromoListRecommendationResponseData()
)

data class GetPromoListRecommendationResponseData(
    @SerializedName("data")
    val data: PromoListRecommendation = PromoListRecommendation()
)

data class PromoListRecommendation(
    @SerializedName("result_status")
    val resultStatus: ResultStatus = ResultStatus(),
    @SerializedName("empty_state")
    val emptyState: EmptyStateResponse = EmptyStateResponse(),
    @SerializedName("promo_recommendation")
    val promoRecommendation: PromoRecommendation = PromoRecommendation(),
    @SerializedName("coupon_sections")
    val couponSections: List<CouponSection> = emptyList(),
    @SerializedName("additional_message")
    val additionalMessage: String = "",
    @SerializedName("entry_point_info")
    val entryPointInfo: EntryPointInfo = EntryPointInfo(),
    @SerializedName("ticker_info")
    val tickerInfo: TickerInfo = TickerInfo(),
    @SerializedName("attempted_promo_code_error")
    val attemptedPromoCodeError: AttemptedPromoCodeError = AttemptedPromoCodeError()
) {
    companion object {
        const val STATUS_OK = "OK"
    }
}

data class ResultStatus(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("success")
    val success: Boolean = false
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
    val message: String = "",
    @SerializedName("message_selected_state")
    val messageSelected: String = "",
    @SerializedName("background_url")
    val backgroundUrl: String = "",
    @SerializedName("animation_url")
    val animationUrl: String = "",
)

data class CouponSection(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("is_collapsed")
    val isCollapsed: Boolean = false,
    @SerializedName("coupons")
    val coupons: List<Coupon>,
    @SerializedName("coupon_groups")
    val couponGroups: List<CouponGroup> = emptyList(),
)

data class Coupon(
    @SerializedName("promo_id")
    val id: String = "",
    @SerializedName("index")
    val index: Int = 0,
    @SerializedName("code")
    val code: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("coupon_type")
    val couponType: List<String> = emptyList(),
    @SerializedName("expiry_info")
    val expiryInfo: String = "",
    @SerializedName("expiry_countdown")
    val expiryCountdown: Long = 0,
    @SerializedName("unique_id")
    val uniqueId: String = "",
    @SerializedName("shop_id")
    val shopId: Long = 0,
    @SerializedName("group_id")
    val groupId: Long = 0,
    @SerializedName("is_selected")
    val isSelected: Boolean = false,
    @SerializedName("is_attempted")
    val isAttempted: Boolean = false,
    @SerializedName("is_bebas_ongkir")
    val isBebasOngkir: Boolean = false,
    @SerializedName("is_highlighted")
    val isHighlighted: Boolean = false,
    @SerializedName("radio_check_state")
    val radioCheckState: String = "",
    @SerializedName("benefit_type_str")
    val benefitTypeStr: String = "",
    @SerializedName("benefit_amount")
    val benefitAmount: Double = 0.0,
    @SerializedName("benefit_amount_str")
    val benefitAmountStr: String = "",
    @SerializedName("coupon_card_details")
    val couponCardDetails: List<CouponCardDetail> = emptyList(),
    @SerializedName("benefit_details")
    val benefitDetails: List<BenefitDetail> = emptyList(),
    @SerializedName("clashing_infos")
    val clashingInfos: List<ClashingInfo> = emptyList(),
    @SerializedName("additional_bo_datas")
    val boAdditionalData: List<AdditionalBoData> = emptyList(),
    @SerializedName("promo_infos")
    val promoInfos: List<PromoInfo> = emptyList(),
    @SerializedName("cta")
    val cta: Cta = Cta(),
    @SerializedName("secondary_promo")
    val secondaryCoupon: List<SecondaryCoupon> = emptyList()
)

data class CouponGroup(
    @SerializedName("catalog_id")
    val id: Long = 0,
    @SerializedName("count")
    val count: Int = 0
)

data class CouponCardDetail(
    @SerializedName("state")
    val state: String = "",
    @SerializedName("color")
    val color: String = "",
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @SerializedName("background_url")
    val backgroundUrl: String = ""
)

data class BenefitDetail(
    @SerializedName("amount_idr")
    val amountIdr: Double = 0.0,
    @SerializedName("benefit_type")
    val benefitType: String = "",
    @SerializedName("data_type")
    val dataType: String = ""
)

data class ClashingInfo(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("message")
    val message: String = ""
)

data class BoClashingInfo(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("message")
    val message: String = ""
)

data class AdditionalBoData(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("unique_id")
    val uniqueId: String = "",
    @SerializedName("cart_string_group")
    val cartStringGroup: String = "",
    @SerializedName("shipping_id")
    val shippingId: Long = 0,
    @SerializedName("sp_id")
    val spId: Long = 0,
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
    @SerializedName("bo_campaign_id")
    val boCampaignId: Long = 0,
    @SerializedName("eta_txt")
    val etaText: String = ""
)

data class PromoInfo(
    @SerializedName("info_type")
    val type: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("icon")
    val icon: String = "",
)

data class Cta(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("text")
    val text: String = "",
    @SerializedName("app_link")
    val applink: String = ""
)

data class SecondaryCoupon(
    @SerializedName("promo_id")
    val id: String = "",
    @SerializedName("index")
    val index: Int = 0,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("coupon_type")
    val couponType: List<String> = emptyList(),
    @SerializedName("expiry_info")
    val expiryInfo: String = "",
    @SerializedName("expiry_countdown")
    val expiryCountdown: Long = 0,
    @SerializedName("unique_id")
    val uniqueId: String = "",
    @SerializedName("shop_id")
    val shopId: Long = 0,
    @SerializedName("group_id")
    val groupId: Long = 0,
    @SerializedName("is_selected")
    val isSelected: Boolean = false,
    @SerializedName("is_attempted")
    val isAttempted: Boolean = false,
    @SerializedName("is_bebas_ongkir")
    val isBebasOngkir: Boolean = false,
    @SerializedName("is_highlighted")
    val isHighlighted: Boolean = false,
    @SerializedName("radio_check_state")
    val radioCheckState: String = "",
    @SerializedName("benefit_type_str")
    val benefitTypeStr: String = "",
    @SerializedName("benefit_amount")
    val benefitAmount: Double = 0.0,
    @SerializedName("benefit_amount_str")
    val benefitAmountStr: String = "",
    @SerializedName("coupon_card_details")
    val couponCardDetails: List<CouponCardDetail> = emptyList(),
    @SerializedName("benefit_details")
    val benefitDetails: List<BenefitDetail> = emptyList(),
    @SerializedName("clashing_infos")
    val clashingInfos: List<ClashingInfo> = emptyList(),
    @SerializedName("bo_clashing_infos")
    val boClashingInfo: List<BoClashingInfo> = emptyList(),
    @SerializedName("additional_bo_datas")
    val boAdditionalData: List<AdditionalBoData> = emptyList(),
    @SerializedName("promo_infos")
    val promoInfos: List<PromoInfo> = emptyList(),
    @SerializedName("cta")
    val cta: Cta = Cta()
)

data class EntryPointInfo(
    @SerializedName("messages")
    val messages: List<String> = emptyList(),
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @SerializedName("state")
    val state: String = "",
    @SerializedName("clickable")
    val clickable: Boolean = false
)

data class TickerInfo(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @SerializedName("background_url")
    val backgroundUrl: String = ""
)

data class AttemptedPromoCodeError(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("message")
    val message: String = ""
)
