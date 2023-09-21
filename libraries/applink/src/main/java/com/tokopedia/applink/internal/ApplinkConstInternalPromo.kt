package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalPromo {

    private const val HOST_PROMO = "promo"

    const val INTERNAL_PROMO = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_PROMO"

    const val PROMO_LIST_DIGITAL = "$INTERNAL_PROMO/list/digital"
    const val PROMO_DETAIL_DIGITAL = "$INTERNAL_PROMO/detail/digital"

    const val PROMO_LIST_FLIGHT = "$INTERNAL_PROMO/list/flight"
    const val PROMO_DETAIL_FLIGHT = "$INTERNAL_PROMO/detail/flight"

    const val PROMO_CAMPAIGN_SHAKE_LANDING_PREFIX = "$INTERNAL_PROMO/campaign-shake-landing"
    const val PROMO_CAMPAIGN_SHAKE_LANDING = "$PROMO_CAMPAIGN_SHAKE_LANDING_PREFIX/{is_long_shake}/"

    /**
     * PromoCheckoutListDealsActivity
     * PromoCheckoutDetailDealsActivity
     */
    const val PROMO_LIST_DEALS = "$INTERNAL_PROMO/list/deals"
    const val PROMO_DETAIL_DEALS = "$INTERNAL_PROMO/detail/deals"

    /**
     * PromoCheckoutListHotelActivity
     * PromoCheckoutDetailHotelActivity
     */
    const val PROMO_LIST_HOTEL = "$INTERNAL_PROMO/list/hotel"
    const val PROMO_DETAIL_HOTEL = "$INTERNAL_PROMO/detail/hotel"

    const val PROMO_LIST_MARKETPLACE = "$INTERNAL_PROMO/list/marketplace"
    const val PROMO_DETAIL_MARKETPLACE = "$INTERNAL_PROMO/detail/marketplace"

    const val PROMO_LIST = "$INTERNAL_PROMO/promo-list"
    const val PROMO_DETAIL = "$INTERNAL_PROMO/promo-detail?slug={slug}"

    const val PROMO_CHECKOUT_MARKETPLACE = "$INTERNAL_PROMO/checkout-marketplace"

    private const val HOST_TOKOPOINTS = "rewards"
    const val INTERNAL_TOKOPOINTS = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_TOKOPOINTS"
    const val TOKOPOINTS_HOME = "$INTERNAL_TOKOPOINTS/home"
    const val TOKOPOINTS_COUPON_LISTING = "$INTERNAL_TOKOPOINTS/kupon-saya/{slug}"
    const val TOKOPOINTS_COUPON_DETAIL = "$INTERNAL_TOKOPOINTS/kupon-detail/{coupon_code}"
    const val TOKOPOINTS_COUPON = "$INTERNAL_TOKOPOINTS/kupon-saya"
    const val TOKOPOINTS_CATALOG_DETAIL = "$INTERNAL_TOKOPOINTS/tukar-detail/{catalog_code}"
    const val TOKOPOINTS_CATALOG_LISTING = "$INTERNAL_TOKOPOINTS/tukar-point/{slug_category}/{slug_sub_category}"

    /**
     * This const used to redirect to PromoCheckoutListUmrahActivity
     * This const used to redirect to PromoCheckoutDetailUmrahActivity
     */
    const val PROMO_LIST_UMROH = "$INTERNAL_PROMO/list/umroh"
    const val PROMO_DETAIL_UMROH = "$INTERNAL_PROMO/detail/umroh"

    /**
     * This const used to redirect to PromoCheckoutListEventActivity
     * This const used to redirect to PromoCheckoutDetailEventActivity
     */
    const val PROMO_LIST_EVENT = "$INTERNAL_PROMO/list/event"
    const val PROMO_DETAIL_EVENT = "$INTERNAL_PROMO/detail/event"

    /**
     * Gamification
     * */
    private const val HOST_GLOBAL = "global"
    const val INTERNAL_GLOBAL = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_GLOBAL"
    const val INTERNAL_GAMIFICATION_CRACK = "$INTERNAL_GLOBAL/gamification"
    const val INTERNAL_GAMIFICATION_TAP_TAP_MANTAP = "$INTERNAL_GLOBAL/gamification2"
    const val INTERNAL_GAMIFICATION_SMC_REFERRAL = "$INTERNAL_GLOBAL/smc-referral"
    const val INTERNAL_GAMIFICATION_DAILY_GIFT = "$INTERNAL_GLOBAL/gamification_gift_daily"
    const val INTERNAL_GAMIFICATION_TAP_TAP_GIFT = "$INTERNAL_GLOBAL/gamification_gift_60s"

    // Scp Rewards
    private const val HOST_SCP_REWARDS = "scp-rewards"
    const val INTERNAL_SCP_REWARDS = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_SCP_REWARDS"
    const val CELEBRATION_PAGE = "$INTERNAL_SCP_REWARDS/celebration/{medali_slug}"
    const val MEDAL_DETAIL = "$INTERNAL_SCP_REWARDS/medali/detail/{medali_slug}"
    const val CELEBRATION_BOTTOMSHEET_BASE = "${DeeplinkConstant.SCHEME_INTERNAL}://scp-rewards/medali/celebration-bottomsheet"
    const val CELEBRATION_BOTTOMSHEET = "$CELEBRATION_BOTTOMSHEET_BASE/{slug}"
    const val CELEBRATION_TOASTER = "${DeeplinkConstant.SCHEME_INTERNAL}://scp-rewards/medali/celebration_toaster"

    const val MEDAL_CABINET = "$INTERNAL_SCP_REWARDS/medali/cabinet"
    const val SEE_MORE_MEDALI = "$INTERNAL_SCP_REWARDS/medali/cabinet/more"
    const val SEE_MORE_PATH = "more"
    const val CABINET_PATH = "cabinet"
    const val MEDALI_QUERY_PARAM = "medali"
    const val SOURCE_PARAM = "sourceName"
}
