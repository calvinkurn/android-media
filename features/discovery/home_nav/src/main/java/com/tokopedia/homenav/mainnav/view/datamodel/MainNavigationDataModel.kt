package com.tokopedia.homenav.mainnav.view.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class MainNavigationDataModel(
        val dataList: List<Visitable<*>> = mutableListOf()
): Visitable<MainNavigationDataModel> {
    override fun type(typeFactory: MainNavigationDataModel): Int {
        return typeFactory.type(this)
    }
}

class MainNavType{
    fun type(model: MainNavigationDataModel): Int = -1
}