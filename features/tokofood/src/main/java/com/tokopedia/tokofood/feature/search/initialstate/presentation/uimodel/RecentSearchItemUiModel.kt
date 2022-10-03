package com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.InitialStateTypeFactoryImpl

data class RecentSearchItemUiModel(
    val sectionId: String,
    val itemId: String,
    val imageUrl: String,
    val title: String,
    val template: String,
    val imageActionUrl: String
) : BaseInitialStateVisitable, ImpressHolder() {
    override fun type(typeFactory: InitialStateTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}