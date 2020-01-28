package com.tokopedia.sellerhome.view.model

import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory

/**
 * Created By @ilhamsuaib on 2020-01-24
 */

data class SectionWidgetUiModel(
        override val widgetType: String,
        override val title: String,
        override val subTitle: String,
        override val tooltip: TooltipUiModel,
        override val url: String,
        override val appLink: String,
        override val dataKey: String,
        override val ctaText: String,
        override val data: SectionDataUiModel?
) : BaseWidgetUiModel<SectionDataUiModel> {

    override fun type(typeFactory: SellerHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}