package com.tokopedia.search.result.product.inspirationcarousel.analytics

import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.Option
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.Option.Product
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

class InspirationCarouselTrackingUnification @Inject constructor() {

    data class Data(
        val keyword: String,
        val product: Product,
        val filterSortParams: String,
    ) {
        val eventLabel: String
            get() = "$keyword - ${product.inspirationCarouselTitle} - ${product.optionTitle}"

        val productDataLayer: Any
            get() = product.asUnificationObjectDataLayer(filterSortParams)
    }

    fun trackCarouselImpression(
        trackingQueue: TrackingQueue,
        data: Data,
    ) {
        SearchTracking.trackEventImpressionInspirationCarouselUnification(
            trackingQueue,
            data.eventLabel,
            arrayListOf(data.productDataLayer)
        )
    }

    fun trackCarouselClick(data: Data) {
        SearchTracking.trackEventClickInspirationCarouselUnification(
            data.eventLabel,
            data.product.inspirationCarouselType,
            data.product.componentId,
            arrayListOf(data.productDataLayer),
        )
    }

    fun trackCarouselClickSeeAll(
        keyword: String,
        option: Option,
    ) {
        val searchComponentTracking = option.asSearchComponentTracking(keyword)

        searchComponentTracking.click(TrackApp.getInstance().gtm)
    }

    private fun Option.asSearchComponentTracking(keyword: String): SearchComponentTracking =
        searchComponentTracking(
            trackingOption = trackingOption,
            keyword = keyword,
            valueId = "0",
            valueName = "$carouselTitle - $title",
            componentId = componentId,
            applink = applink,
            dimension90 = dimension90
        )
}
