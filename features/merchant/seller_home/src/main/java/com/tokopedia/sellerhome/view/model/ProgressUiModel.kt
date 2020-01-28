package com.tokopedia.sellerhome.view.model

import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory

/**
 * Created By @yusufhendrawan on 2020-01-22
 */
class ProgressUiModel(
        override val widgetType: String,
        override val title: String,
        override val url: String,
        override val appLink: String,
        var currentProgress: Float,
        var state: State,
        var description: String
) : BaseWidgetUiModel {
    override fun type(typeFactory: SellerHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    enum class State {
        GREEN,
        ORANGE,
        RED
    }
}