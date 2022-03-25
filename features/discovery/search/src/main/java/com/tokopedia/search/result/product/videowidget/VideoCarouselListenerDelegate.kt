package com.tokopedia.search.result.product.videowidget

import android.content.Context
import com.tokopedia.search.analytics.InspirationCarouselTrackingUnification
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselTrackingUnificationDataMapper
import com.tokopedia.search.result.product.querykeyprovider.QueryKeyProvider
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.trackingoptimizer.TrackingQueue

class VideoCarouselListenerDelegate(
    context: Context?,
    private val trackingQueue: TrackingQueue?,
    private val inspirationCarouselTrackingUnification: InspirationCarouselTrackingUnification,
    queryKeyProvider: QueryKeyProvider,
    carouselTrackingUnificationDataMapper: InspirationCarouselTrackingUnificationDataMapper,
    applinkOpener: ApplinkOpener = ApplinkOpenerDelegate
) : InspirationVideoCarouselListener,
    InspirationCarouselTrackingUnificationDataMapper by carouselTrackingUnificationDataMapper,
    ApplinkOpener by applinkOpener,
    QueryKeyProvider by queryKeyProvider,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onInspirationVideoCarouselProductImpressed(product: InspirationCarouselDataView.Option.Product) {
        val trackingQueue = trackingQueue ?: return

        val data = createCarouselTrackingUnificationData(product)

        inspirationCarouselTrackingUnification.trackCarouselImpression(trackingQueue, data) {
            val products = ArrayList<Any>()
            products.add(product.getInspirationCarouselListProductImpressionAsObjectDataLayer())

            SearchTracking.trackImpressionInspirationCarouselList(
                trackingQueue,
                product.inspirationCarouselType,
                queryKey,
                products
            )
        }
    }

    override fun onInspirationVideoCarouselProductClicked(product: InspirationCarouselDataView.Option.Product) {
        openApplink(context, product.applink)

        val data = createCarouselTrackingUnificationData(product)
        inspirationCarouselTrackingUnification.trackCarouselClick(data) {
            val products = ArrayList<Any>()
            products.add(product.getInspirationCarouselListProductAsObjectDataLayer())

            SearchTracking.trackEventClickInspirationCarouselListProduct(
                product.inspirationCarouselType,
                queryKey,
                products,
            )
        }
    }
}