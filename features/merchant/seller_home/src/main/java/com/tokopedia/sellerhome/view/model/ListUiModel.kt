package com.tokopedia.sellerhome.view.model

import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory

class ListUiModel(
        override val widgetType: String,
        override val title: String,
        override val url: String,
        override val appLink: String,
        val listItems: MutableList<ListItemUiModel>
) : BaseWidgetUiModel {
    override fun type(typeFactory: SellerHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}