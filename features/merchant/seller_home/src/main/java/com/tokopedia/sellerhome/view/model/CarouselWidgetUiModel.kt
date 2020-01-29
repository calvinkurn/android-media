package com.tokopedia.sellerhome.view.model

import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory

/**
 * Created By @faisalramd on 2020-01-23
 */

class CarouselWidgetUiModel(
        override val widgetType: String,
        override val title: String,
        override val subTitle: String,
        override val tooltip: TooltipUiModel,
        override val url: String,
        override val appLink: String,
        override val dataKey: String,
        override val ctaText: String,
        override var data: CarouselDataUiModel?
) : BaseWidgetUiModel<CarouselDataUiModel> {

    override fun type(typeFactory: SellerHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}

enum class CarouselState {
    LOADING,
    NORMAL,
    ERROR,
    IMPERFECT_WITHOUT_SEE_ALL,
    IMPERFECT_WITHOUT_BOTTOM_ITEM
}