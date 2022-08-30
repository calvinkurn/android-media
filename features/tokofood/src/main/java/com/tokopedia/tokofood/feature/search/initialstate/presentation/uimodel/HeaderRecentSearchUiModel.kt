package com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel

import com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.InitialStateTypeFactoryImpl

data class HeaderRecentSearchUiModel(
    val headerLabel: String,
    val labelText: String,
    val labelAction: String
): BaseInitialStateVisitable {
    override fun type(typeFactory: InitialStateTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}