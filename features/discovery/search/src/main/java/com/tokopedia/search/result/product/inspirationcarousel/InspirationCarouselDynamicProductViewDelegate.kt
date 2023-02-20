package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.product.broadmatch.BroadMatchItemDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTracking
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

@SearchScope
class InspirationCarouselDynamicProductViewDelegate @Inject constructor(
    private val trackingQueue: TrackingQueue,
    searchParameterProvider: SearchParameterProvider,
    queryKeyProvider: QueryKeyProvider,
): InspirationCarouselDynamicProductView,
    SearchParameterProvider by searchParameterProvider,
    QueryKeyProvider by queryKeyProvider {

    override fun trackDynamicProductCarouselImpression(
        dynamicProductCarousel: BroadMatchItemDataView,
        type: String,
        inspirationCarouselProduct: InspirationCarouselDataView.Option.Product
    ) {
        val trackingQueue = trackingQueue
        val data = createCarouselTrackingUnificationData(
            inspirationCarouselProduct,
            getSearchParameter()
        )

        InspirationCarouselTracking.trackCarouselImpression(trackingQueue, data)
    }

    override fun trackDynamicProductCarouselClick(
        dynamicProductCarousel: BroadMatchItemDataView,
        type: String,
        inspirationCarouselProduct: InspirationCarouselDataView.Option.Product,
    ) {
        val data = createCarouselTrackingUnificationData(
            inspirationCarouselProduct,
            getSearchParameter()
        )

        InspirationCarouselTracking.trackCarouselClick(data)
    }

    override fun trackEventClickSeeMoreDynamicProductCarousel(
        dynamicProductCarousel: BroadMatchDataView,
        type: String,
        inspirationCarouselOption: InspirationCarouselDataView.Option
    ) {
        InspirationCarouselTracking.trackCarouselClickSeeAll(
            queryKey,
            inspirationCarouselOption,
        )
    }
}
