package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.search.analytics.InspirationCarouselTrackingUnification
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView

interface InspirationCarouselTrackingUnificationDataMapper {
    fun createCarouselTrackingUnificationData(
        product: InspirationCarouselDataView.Option.Product
    ): InspirationCarouselTrackingUnification.Data
}