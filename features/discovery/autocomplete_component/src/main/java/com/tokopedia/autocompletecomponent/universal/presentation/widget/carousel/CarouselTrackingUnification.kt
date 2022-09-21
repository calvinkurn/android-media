package com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel

import com.tokopedia.autocompletecomponent.universal.analytics.UniversalTracking
import com.tokopedia.autocompletecomponent.universal.presentation.BaseUniversalDataView
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

class CarouselTrackingUnification @Inject constructor()  {
    data class Data(
        val keyword: String,
        val product: CarouselDataView.Product,
    ) {
        val eventLabel: String
            get() = "$keyword - ${product.carouselTitle} - "

        val productDataLayer: Any
            get() = product.asUnificationObjectDataLayer()
    }

    fun trackCarouselImpression(
        trackingQueue: TrackingQueue,
        data: Data,
    ) {
        UniversalTracking.trackEventImpressionCarouselUnification(
            trackingQueue,
            data.eventLabel,
            arrayListOf(data.productDataLayer),
        )
    }

    fun trackCarouselClick(data: Data) {
        UniversalTracking.trackEventClickCarouselUnification(
            data.eventLabel,
            data.product.carouselType,
            data.product.componentId,
            arrayListOf(data.productDataLayer),
        )
    }

    fun trackCarouselClickSeeAll(
        keyword: String,
        universalDataView: BaseUniversalDataView,
    ) {
        val searchComponentTracking = universalDataView.asSearchComponentTracking(keyword)

        searchComponentTracking.click(TrackApp.getInstance().gtm)
    }

    private fun BaseUniversalDataView.asSearchComponentTracking(keyword: String): SearchComponentTracking =
        searchComponentTracking(
            trackingOption = trackingOption,
            keyword = keyword,
            valueId = "0",
            valueName = "none",
            componentId = componentId,
            applink = applink,
            dimension90 = dimension90,
        )
}