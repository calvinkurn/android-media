package com.tokopedia.sellerhome.view.model

import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory

/**
 * Created By @ilhamsuaib on 2020-01-24
 */

data class SectionWidgetUiModel(
        override val widgetType: String,
        override val title: String,
        override val url: String,
        override val appLink: String
) : BaseWidgetUiModel {

    override fun type(typeFactory: SellerHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}