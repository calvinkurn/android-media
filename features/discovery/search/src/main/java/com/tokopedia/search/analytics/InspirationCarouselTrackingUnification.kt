package com.tokopedia.search.analytics

import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingRollence
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView.Option
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView.Option.Product
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_CAROUSEL_CONTENT_TRACKER_UNIFICATION as ROLLENCE_KEY

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

    private fun isUseUnification(): Boolean {
        return try {
            val remoteConfig = RemoteConfigInstance.getInstance().abTestPlatform ?: return false

            remoteConfig.getString(ROLLENCE_KEY, "") == ROLLENCE_KEY
        } catch (throwable: Throwable) {
            false
        }
    }

    fun trackCarouselImpression(
        trackingQueue: TrackingQueue,
        data: Data,
        fallback: () -> Unit,
    ) {
        if (isUseUnification())
            SearchTracking.trackEventImpressionInspirationCarouselUnification(
                trackingQueue,
                data.eventLabel,
                listOf(data.productDataLayer)
            )
        else
            fallback()
    }

    fun trackCarouselClick(data: Data, fallback: () -> Unit) {
        if (isUseUnification())
            SearchTracking.trackEventClickInspirationCarouselUnification(
                data.eventLabel,
                data.product.inspirationCarouselType,
                data.product.componentId,
                listOf(data.productDataLayer),
            )
        else
            fallback()
    }

    fun trackCarouselClickSeeAll(
        keyword: String,
        option: Option,
        fallback: () -> Unit,
    ) {
        val searchComponentTracking = option.asSearchComponentTracking(keyword)

        SearchComponentTrackingRollence.click(searchComponentTracking, ROLLENCE_KEY, fallback)
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
