package com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel

import com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.InitialStateTypeFactoryImpl

data class SeeMoreCuisineUiModel(
    val sectionId: String,
    val cuisineMoreList: List<CuisineItemUiModel>
): BaseInitialStateVisitable {
    override fun type(typeFactory: InitialStateTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}