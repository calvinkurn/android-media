package com.tokopedia.scp_rewards.cabinet.analytics

interface MedalCabinetAnalytics {

    fun sendViewMedalCabinetPageEvent()
    fun sendClickBackMedalCabinetPageEvent()
    fun sendClickUnlockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String
    )

    fun sendViewUnlockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String
    )

    fun sendViewUnlockedMedalGenericEvent(badgeId: String)
    fun sendViewUnlockedMedalSectionCtaEvent(buttonText: String)
    fun sendClickUnlockedMedalSectionCtaEvent(buttonText: String)
    fun sendViewLockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String,
        taskProgressPercent: String
    )

    fun sendClickLockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String,
        taskProgressPercent: String
    )

    fun sendViewLockedMedalSectionCtaEvent(buttonText: String)
    fun sendClickLockedMedalSectionCtaEvent(buttonText: String)
    fun sendViewSeeMoreUnlockedMedalPageEvent()
    fun sendViewSeeMoreUnlockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String
    )

    fun sendClickSeeMoreUnlockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String
    )

    fun sendClickBackSeeMoreUnlockedMedalEvent()
    fun sendViewSeeMoreLockedMedalPageEvent()
    fun sendViewSeeMoreLockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String,
        taskProgressPercent: String
    )

    fun sendClickSeeMoreLockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String,
        taskProgressPercent: String
    )

    fun sendClickBackSeeMoreLockedMedalEvent()
    fun sendViewBannerEvent(creativeName: String, bannerPosition: String)
    fun sendClickBannerEvent(creativeName: String, bannerPosition: String)
    fun sendViewUnlockedMedalSectionApiErrorEvent()
    fun sendClickCtaUnlockedMedalSectionApiErrorEvent(buttonText: String)
    fun sendViewLockedMedalSectionApiErrorEvent()
    fun sendClickCtaLockedMedalSectionApiErrorEvent(buttonText: String)
    fun sendViewMedalCabinetPageApiErrorEvent()
    fun sendViewMedalCabinetSkeletonPageEvent()
    fun sendViewMedalCabinetPageInternetErrorEvent()
    fun sendClickCobaLagiMedalCabinetPageApiErrorEvent()
    fun sendClickHalamanUtamaMedalCabinetPageApiErrorEvent()
    fun sendClickBackMedalCabinetPageApiErrorEvent()
    fun sendClickCobaLagiMedalCabinetPageInternetErrorEvent()
    fun sendClickBackMedalCabinetPageInternetErrorEvent()
    fun sendViewMedalCabinetPageNonWhitelistedErrorEvent()
    fun sendClickCtaMedalCabinetPageNonWhitelistedErrorEvent(buttonText: String)
    fun sendClickBackMedalCabinetPageNonWhitelistedErrorEvent()
    fun sendViewSeeMoreUnlockedMedalPageNonWhitelistedErrorEvent()
    fun sendClickCtaSeeMoreUnlockedMedalPageNonWhitelistedErrorEvent()
    fun sendClickBackSeeMoreUnlockedMedalPageNonWhitelistedErrorEvent()
    fun sendViewSeeMoreLockedMedalPageNonWhitelistedErrorEvent()
    fun sendClickCtaSeeMoreLockedMedalPageNonWhitelistedErrorEvent()
    fun sendClickBackSeeMoreLockedMedalPageNonWhitelistedErrorEvent()
}
