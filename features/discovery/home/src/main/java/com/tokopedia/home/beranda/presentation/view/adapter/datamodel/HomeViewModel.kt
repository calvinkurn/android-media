package com.tokopedia.home.beranda.presentation.view.adapter.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel

data class HomeViewModel(
        val homeFlag: HomeFlag = HomeFlag(),
        val list: MutableList<Visitable<*>> = mutableListOf(),
        val isCache: Boolean = false
) : Visitable<HomeViewType> {

    override fun type(typeFactory: HomeViewType): Int {
        return typeFactory.type(this)
    }

    fun isContainsHomePlay(): Int{
        return list.withIndex().find { it.value is PlayCardViewModel }?.index ?: -1
    }

    fun getListWithoutHomePlay(): List<Visitable<*>>{
        return list.filter{ it !is PlayCardViewModel}
    }

    fun removeHomePlay(){
        val temp = list.filter{ it !is PlayCardViewModel}
        list.clear()
        list.addAll(temp)
    }

    fun getHomePlay() = list.find{ it is PlayCardViewModel }
}

class HomeViewType{
    fun type(homeViewModel: HomeViewModel): Int = -1
}