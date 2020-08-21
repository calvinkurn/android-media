package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class CarouselWidgetUiModel(
        override val widgetType: String,
        override val title: String,
        override val subtitle: String,
        override val tooltip: TooltipUiModel?,
        override val url: String,
        override val appLink: String,
        override val dataKey: String,
        override val ctaText: String,
        override var data: CarouselDataUiModel?,
        override val impressHolder: ImpressHolder = ImpressHolder(),
        override var isLoaded: Boolean
) : BaseWidgetUiModel<CarouselDataUiModel> {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}