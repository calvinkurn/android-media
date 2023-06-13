package com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.InitialStateTypeFactoryImpl

data class ChipsListUiModel(
    val chipsPopularSearchList: List<ChipsPopularSearch> = emptyList()
) : BaseInitialStateVisitable {
    override fun type(typeFactory: InitialStateTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}

data class ChipsPopularSearch(
    val id: String,
    val title: String,
    val appLink: String
): ImpressHolder()
