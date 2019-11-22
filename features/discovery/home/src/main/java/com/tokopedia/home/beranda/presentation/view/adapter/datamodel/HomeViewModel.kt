package com.tokopedia.home.beranda.presentation.view.adapter.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable

data class HomeViewModel(
        val homeFlag: HomeFlag = HomeFlag(),
        val list: List<HomeVisitable<Any>> = listOf(),
        val isCache: Boolean = false
) : Visitable<HomeViewType> {

    override fun type(typeFactory: HomeViewType): Int {
        return typeFactory.type(this)
    }
}

class HomeViewType{
    fun type(homeViewModel: HomeViewModel): Int = -1
}