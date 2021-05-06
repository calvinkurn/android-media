package com.tokopedia.homenav.mainnav.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.mainnav.view.datamodel.SeparatorDataModel

class BuListMapper {

    fun mapToBuListModel(categoryData: List<DynamicHomeIconEntity.Category>?): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()
        categoryData?.toVisitable()?.let { visitableList.addAll(it) }
        return visitableList
    }
}