package com.tokopedia.homenav.mainnav.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable

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