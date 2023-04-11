package com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel

import com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.InitialStateTypeFactoryImpl

data class SeeMoreRecentSearchUiModel(
    val sectionId: String,
    val recentSearchMoreList: List<RecentSearchItemUiModel>
): BaseInitialStateVisitable {
    override fun type(typeFactory: InitialStateTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}