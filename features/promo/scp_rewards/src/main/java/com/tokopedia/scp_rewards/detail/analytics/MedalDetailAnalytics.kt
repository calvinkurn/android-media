package com.tokopedia.scp_rewards.detail.analytics

interface MedalDetailAnalytics {

    fun sendImpressionMDP(badgeId: String)

    fun sendClickBackButton(badgeId: String)

    fun sendClickTncCta(badgeId: String)

    fun sendImpressionMedali(
        badgeId: String,
        isLocked: Boolean,
        poweredBy: String,
        medalTitle: String,
        medalDescription: String
    )

    fun sendClickMedali(
        badgeId: String,
        isLocked: Boolean,
        poweredBy: String,
        medalTitle: String,
        medalDescription: String
    )

    fun sendImpressionProgressSection(
        badgeId: String,
        isLocked: Boolean,
        taskProgressPercent: String,
        noOfTasksCompleted: String
    )

    fun sendImpressionBonusCoupon(
        badgeId: String,
        promoCode: String,
        couponStatus: String,
        couponNotes: String
    )

    fun sendImpressionCabinetCta(
        badgeId: String,
        promoCode: String,
        couponStatus: String,
        couponNotes: String
    )

    fun sendClickCabinetCta(
        badgeId: String,
        promoCode: String,
        couponStatus: String,
        couponNotes: String
    )

    fun sendImpressionShopCta(
        badgeId: String,
        promoCode: String,
        couponStatus: String,
        couponNotes: String,
        ctaText: String
    )

    fun sendClickShopCta(
        badgeId: String,
        promoCode: String,
        couponStatus: String,
        couponNotes: String,
        ctaText: String
    )

    fun sendImpressionAutoApplyToaster(
        badgeId: String,
        promoCode: String,
        couponStatus: String,
        couponNotes: String,
        isAutoApplySuccess: Boolean
    )

    fun sendImpressionCouponError(
        badgeId: String,
        promoCode: String
    )

    fun sendImpressionPageShimmer(badgeId: String)

    fun sendImpressionNonWhitelistedError()

    fun sendNonWhitelistedUserCtaClick()

    fun sendNonWhitelistedBackClick()
}
