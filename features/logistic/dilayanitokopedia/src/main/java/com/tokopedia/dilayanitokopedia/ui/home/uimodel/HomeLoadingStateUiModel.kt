package com.tokopedia.dilayanitokopedia.ui.home.uimodel

import com.tokopedia.dilayanitokopedia.ui.home.adapter.DtHomeAdapterTypeFactory

data class HomeLoadingStateUiModel(
    val id: String
) : HomeLayoutUiModel(id) {

    override fun type(typeFactory: DtHomeAdapterTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
