package com.tokopedia.home.beranda.presentation.view.adapter.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.HomeFlag

data class HomeDataModel(
        val homeFlag: HomeFlag = HomeFlag(),
        val list: List<Visitable<*>> = listOf(),
        val isCache: Boolean = false,
        val isFirstPage: Boolean = false,
        val isProcessingAtf: Boolean = true,
        val isProcessingDynamicChannle: Boolean = false
) : Visitable<HomeViewType> {

    override fun type(typeFactory: HomeViewType): Int {
        return typeFactory.type(this)
    }
}

class HomeViewType{
    fun type(homeDataModel: HomeDataModel): Int = -1
}