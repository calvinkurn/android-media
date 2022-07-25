package com.tokopedia.officialstore.official.presentation.adapter.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class OfficialStoreDataModel(
    val dataList: List<Visitable<*>> = mutableListOf()
): Visitable<OfficialStoreDataModel> {
    override fun type(typeFactory: OfficialStoreDataModel): Int {
        return typeFactory.type(this)
    }
}