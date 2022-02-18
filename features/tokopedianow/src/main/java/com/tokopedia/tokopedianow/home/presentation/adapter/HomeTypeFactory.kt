package com.tokopedia.tokopedianow.home.presentation.adapter

import com.tokopedia.tokopedianow.home.presentation.uimodel.*

interface HomeTypeFactory {
    fun type(uiModel: HomeTickerUiModel): Int
    fun type(uiModel: HomeProductRecomUiModel): Int
    fun type(uiModel: HomeEmptyStateUiModel): Int
    fun type(uiModel: HomeLoadingStateUiModel): Int
    fun type(uiModel: HomeSharingEducationWidgetUiModel): Int
    fun type(uiModel: HomeEducationalInformationWidgetUiModel): Int
    fun type(uiModel: HomeProgressBarUiModel): Int
    fun type(uiModel: HomeQuestSequenceWidgetUiModel): Int
    fun type(uiModel: HomeQuestWidgetUiModel): Int
    fun type(uiModel: HomeQuestTitleUiModel): Int
    fun type(uiModel: HomeQuestAllClaimedWidgetUiModel): Int
    fun type(uiModel: HomeSwitcherUiModel): Int
}
