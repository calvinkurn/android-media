package com.tokopedia.sellerhome.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhome.view.adapter.ListAdapterTypeFactory

class ListItemUiModel(
        val imageUrl: String,
        val title: String,
        val type: String,
        val desc: String
) : Visitable<ListAdapterTypeFactory> {
    override fun type(typeFactory: ListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}