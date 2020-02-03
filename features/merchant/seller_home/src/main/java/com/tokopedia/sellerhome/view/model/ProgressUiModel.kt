package com.tokopedia.sellerhome.view.model

import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory

/**
 * Created By @yusufhendrawan on 2020-01-22
 */
class ProgressUiModel(
        override val widgetType: String,
        override val title: String,
        override val appLink: String,
        override val subTitle: String,
        override val tooltip: TooltipUiModel?,
        override val url: String,
        override val dataKey: String,
        override val ctaText: String,
        override var data: ProgressDataUiModel?
) : BaseWidgetUiModel<ProgressDataUiModel> {
    override fun type(typeFactory: SellerHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}