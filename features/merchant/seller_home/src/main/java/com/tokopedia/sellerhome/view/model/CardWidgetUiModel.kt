package com.tokopedia.sellerhome.view.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory

/**
 * Created By @ilhamsuaib on 2020-01-21
 */

data class CardWidgetUiModel(
        override val widgetType: String,
        override val title: String,
        override val subtitle: String,
        override val tooltip: TooltipUiModel?,
        override val url: String,
        override val appLink: String,
        override val dataKey: String,
        override val ctaText: String,
        override var data: CardDataUiModel?,
        override val impressHolder: ImpressHolder= ImpressHolder(),
        override var isLoaded: Boolean
) : BaseWidgetUiModel<CardDataUiModel> {

    override fun type(typeFactory: SellerHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}