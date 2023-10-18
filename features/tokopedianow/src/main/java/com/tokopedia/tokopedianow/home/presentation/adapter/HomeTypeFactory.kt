package com.tokopedia.tokopedianow.home.presentation.adapter

import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemShimmeringUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEducationalInformationWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEmptyStateUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLoadingStateUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomePlayWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCarouselChipsUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProgressBarUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestAllClaimedWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestTitleUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSwitcherUiModel


interface HomeTypeFactory {
    fun type(uiModel: HomeSharingWidgetUiModel): Int
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
    fun type(uiModel: HomeProductCarouselChipsUiModel): Int
    fun type(uiModel: HomeHeaderUiModel): Int
}
