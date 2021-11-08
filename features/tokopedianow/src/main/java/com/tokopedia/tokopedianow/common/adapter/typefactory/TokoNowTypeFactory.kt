package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.*

interface TokoNowTypeFactory {
    fun type(uiModel: TokoNowCategoryGridUiModel): Int
    fun type(uiModel: TokoNowRepurchaseUiModel): Int
    fun type(uiModel: TokoNowChooseAddressWidgetUiModel): Int
    fun type(uiModel: TokoNowEmptyStateOocUiModel): Int
    fun type(uiModel: TokoNowRecommendationCarouselUiModel): Int
    fun type(uiModel: TokoNowEmptyStateNoResultUiModel): Int
    fun type(uiModel: TokoNowServerErrorUiModel): Int
}