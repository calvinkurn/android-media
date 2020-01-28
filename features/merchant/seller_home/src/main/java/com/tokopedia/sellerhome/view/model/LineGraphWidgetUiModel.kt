package com.tokopedia.sellerhome.view.model

import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

data class LineGraphWidgetUiModel(
        override val widgetType: String,
        override val title: String,
        override val subTitle: String,
        override val tooltip: TooltipUiModel,
        override val url: String,
        override val appLink: String,
        override val dataKey: String,
        override val ctaText: String,
        override val data: LineGraphDataUiModel?
) : BaseWidgetUiModel<LineGraphDataUiModel> {

    override fun type(typeFactory: SellerHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}