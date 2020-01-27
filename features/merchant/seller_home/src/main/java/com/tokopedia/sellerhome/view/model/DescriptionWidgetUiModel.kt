package com.tokopedia.sellerhome.view.model

import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory

class DescriptionWidgetUiModel(
        override val widgetType: String,
        override val title: String,
        override val url: String,
        override val appLink: String,
        var state: DescriptionState,
        var description: String) : BaseWidgetUiModel {
    override fun type(typeFactory: SellerHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}

enum class DescriptionState {
    LOADING,
    ERROR,
    IDEAL
}