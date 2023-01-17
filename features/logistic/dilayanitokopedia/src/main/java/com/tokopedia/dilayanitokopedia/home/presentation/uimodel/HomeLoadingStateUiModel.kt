package com.tokopedia.dilayanitokopedia.home.presentation.uimodel

import com.tokopedia.dilayanitokopedia.home.presentation.adapter.DtHomeAdapterTypeFactory

data class HomeLoadingStateUiModel(
    val id: String
) : HomeLayoutUiModel(id) {

    override fun type(typeFactory: DtHomeAdapterTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}
