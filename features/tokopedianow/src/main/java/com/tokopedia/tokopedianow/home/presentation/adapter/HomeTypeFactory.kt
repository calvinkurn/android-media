package com.tokopedia.tokopedianow.home.presentation.adapter

import com.tokopedia.tokopedianow.home.presentation.uimodel.*
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemShimmeringUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetUiModel

interface HomeTypeFactory {
    fun type(uiModel: HomeSharingWidgetUiModel): Int
    fun type(uiModel: HomeTickerUiModel): Int
    fun type(uiModel: HomeProductRecomUiModel): Int
    fun type(uiModel: HomeEmptyStateUiModel): Int
    fun type(uiModel: HomeLoadingStateUiModel): Int
    fun type(uiModel: HomeEducationalInformationWidgetUiModel): Int
    fun type(uiModel: HomeProgressBarUiModel): Int
    fun type(uiModel: HomeQuestSequenceWidgetUiModel): Int
    fun type(uiModel: HomeQuestWidgetUiModel): Int
    fun type(uiModel: HomeQuestTitleUiModel): Int
    fun type(uiModel: HomeQuestAllClaimedWidgetUiModel): Int
    fun type(uiModel: HomeSwitcherUiModel): Int
    fun type(uiModel: HomeLeftCarouselAtcUiModel): Int
    fun type(uiModel: HomePlayWidgetUiModel): Int
    fun type(uiModel: HomeClaimCouponWidgetItemUiModel): Int
    fun type(uiModel: HomeClaimCouponWidgetUiModel): Int
    fun type(uiModel: HomeClaimCouponWidgetItemShimmeringUiModel): Int
}
