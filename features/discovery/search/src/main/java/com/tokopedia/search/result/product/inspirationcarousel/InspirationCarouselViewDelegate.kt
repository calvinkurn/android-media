package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTracking
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

class InspirationCarouselViewDelegate @Inject constructor(
    queryKeyProvider: QueryKeyProvider,
    searchParameterProvider: SearchParameterProvider,
    private val trackingQueue: TrackingQueue,
) : InspirationCarouselView,
    QueryKeyProvider by queryKeyProvider,
    SearchParameterProvider by searchParameterProvider {

    override fun trackEventImpressionInspirationCarouselGridItem(product: InspirationCarouselDataView.Option.Product) {
        val trackingQueue = trackingQueue
        val data =
            InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData(
                product,
                getSearchParameter()
            )

        InspirationCarouselTracking.trackCarouselImpression(trackingQueue, data)
    }

    override fun trackEventImpressionInspirationCarouselListItem(product: InspirationCarouselDataView.Option.Product) {
        val trackingQueue = trackingQueue
        val data =
            InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData(
                product,
                getSearchParameter()
            )

        InspirationCarouselTracking.trackCarouselImpression(trackingQueue, data)
    }

    override fun trackEventClickInspirationCarouselGridItem(product: InspirationCarouselDataView.Option.Product) {
        val data =
            InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData(
                product,
                getSearchParameter()
            )

        InspirationCarouselTracking.trackCarouselClick(data)
    }

    override fun trackEventClickInspirationCarouselListItem(product: InspirationCarouselDataView.Option.Product) {
        val data =
            InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData(
                product,
                getSearchParameter()
            )

        InspirationCarouselTracking.trackCarouselClick(data)
    }

    override fun trackEventImpressionInspirationCarouselChipsItem(product: InspirationCarouselDataView.Option.Product) {
        val data =
            InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData(
                product,
                getSearchParameter()
            )
        val trackingQueue = trackingQueue

        InspirationCarouselTracking.trackCarouselImpression(trackingQueue, data)
    }

    override fun trackEventClickInspirationCarouselChipsItem(product: InspirationCarouselDataView.Option.Product) {
        val data =
            InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData(
                product,
                getSearchParameter()
            )

        InspirationCarouselTracking.trackCarouselClick(data)
    }

    override fun trackInspirationCarouselChipsClicked(option: InspirationCarouselDataView.Option) {
        InspirationCarouselTracking.trackCarouselClickSeeAll(queryKey, option)
    }

}
