package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.product.broadmatch.BroadMatchItemDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.Option.Product

interface InspirationCarouselDynamicProductView {

    fun trackDynamicProductCarouselImpression(
        dynamicProductCarousel: BroadMatchItemDataView,
        type: String,
        inspirationCarouselProduct: Product,
    )

    fun trackDynamicProductCarouselClick(
        dynamicProductCarousel: BroadMatchItemDataView,
        type: String,
        inspirationCarouselProduct: Product,
    )

    fun trackEventClickSeeMoreDynamicProductCarousel(
        dynamicProductCarousel: BroadMatchDataView,
        type: String,
        inspirationCarouselOption: InspirationCarouselDataView.Option,
    )
}
