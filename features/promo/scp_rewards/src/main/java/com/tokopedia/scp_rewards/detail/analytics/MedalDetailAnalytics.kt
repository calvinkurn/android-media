package com.tokopedia.scp_rewards.detail.analytics

interface MedalDetailAnalytics {

    fun sendImpressionMDP(badgeId: String)

    fun sendClickBackButton(badgeId: String)

    fun sendClickTncCta(badgeId: String)

    fun sendImpressionMedali(badgeId: String)

    fun sendClickMedali(badgeId: String)

    fun sendImpressionProgressSection(badgeId: String)

    fun sendImpressionBonusCoupon(badgeId: String)

    fun sendImpressionCabinetCta(badgeId: String)

    fun sendClickCabinetCta(badgeId: String)

    fun sendImpressionShopCta(badgeId: String)

    fun sendClickShopCta(badgeId: String)

    fun sendImpressionAutoApplySuccessToaster(badgeId: String)

    fun sendImpressionPageShimmer(badgeId: String)

    fun sendImpressionCouponError(badgeId: String)

    fun sendImpressionCoachmark(badgeId: String)

}
