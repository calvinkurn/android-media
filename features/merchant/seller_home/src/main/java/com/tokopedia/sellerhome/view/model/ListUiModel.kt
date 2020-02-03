package com.tokopedia.sellerhome.view.model

import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory

class ListUiModel(
        override val widgetType: String,
        override val title: String,
        override val subTitle: String,
        override val tooltip: TooltipUiModel?,
        override val url: String,
        override val appLink: String,
        override val dataKey: String,
        override val ctaText: String,
        override var data: ListDataUiModel?
) : BaseWidgetUiModel<ListDataUiModel> {
    override fun type(typeFactory: SellerHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}