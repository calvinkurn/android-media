package com.tokopedia.sellerhome.view.model

import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory

/**
 * Created By @faisalramd on 2020-01-23
 */

class CarouselWidgetUiModel(
        override val widgetType: String,
        override val title: String,
        override val url: String,
        override val appLink: String,
        val imageUrls: List<String>
) : BaseWidgetUiModel {

    override fun type(typeFactory: SellerHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}