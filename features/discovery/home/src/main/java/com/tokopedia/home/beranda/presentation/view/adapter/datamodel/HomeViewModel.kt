package com.tokopedia.home.beranda.presentation.view.adapter.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel

data class HomeViewModel(
        val homeFlag: HomeFlag = HomeFlag(),
        val list: List<Visitable<*>> = listOf(),
        val isCache: Boolean = false
) : Visitable<HomeViewType> {

    override fun type(typeFactory: HomeViewType): Int {
        return typeFactory.type(this)
    }

    fun isContainsHomePlay(): Boolean{
        return list.filterIsInstance<PlayCardViewModel>().isNotEmpty()
    }
}

class HomeViewType{
    fun type(homeViewModel: HomeViewModel): Int = -1
}