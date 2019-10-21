package com.tokopedia.home.beranda.presentation.view.adapter.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.HomeData

class HomeViewModel(
        val homeData: HomeData
): Visitable<HomeViewType> {
    override fun type(typeFactory: HomeViewType): Int {
        return typeFactory.type(this)
    }
}

class HomeViewType{
    fun type(homeViewModel: HomeViewModel): Int = -1
}