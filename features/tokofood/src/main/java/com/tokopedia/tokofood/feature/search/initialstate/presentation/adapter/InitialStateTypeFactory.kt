package com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter

import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.ChipsListUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.CuisineItemUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.HeaderItemInitialStateUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.HeaderRecentSearchUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.RecentSearchItemUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.SeeMoreCuisineUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.SeeMoreRecentSearchUiModel

internal interface InitialStateTypeFactory {
    fun type(type: HeaderItemInitialStateUiModel): Int
    fun type(type: HeaderRecentSearchUiModel): Int
    fun type(type: RecentSearchItemUiModel): Int
    fun type(type: SeeMoreCuisineUiModel): Int
    fun type(type: SeeMoreRecentSearchUiModel): Int
    fun type(type: ChipsListUiModel): Int
    fun type(type: CuisineItemUiModel): Int
}