package com.tokopedia.search.result.product.videowidget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.videowidget.VideoCarouselDataViewMapper.toVideoCarouselDataModel
import com.tokopedia.video_widget.carousel.VideoCarouselDataView
import com.tokopedia.video_widget.carousel.VideoCarouselDataWrapper

data class InspirationCarouselVideoDataView(
    val data: InspirationCarouselDataView
) : Visitable<ProductListTypeFactory>, VideoCarouselDataWrapper {
    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getVideoCarouselData(): VideoCarouselDataView? {
        val option = data.options.firstOrNull() ?: return null
        return option.toVideoCarouselDataModel()
    }
}