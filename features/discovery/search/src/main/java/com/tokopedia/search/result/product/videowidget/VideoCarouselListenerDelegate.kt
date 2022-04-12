package com.tokopedia.search.result.product.videowidget

import android.content.Context
import com.tokopedia.search.analytics.InspirationCarouselTrackingUnification
import com.tokopedia.search.analytics.InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.product.querykeyprovider.QueryKeyProvider
import com.tokopedia.search.result.product.searchparameterprovider.SearchParameterProvider
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
    searchParameterProvider: SearchParameterProvider,
    applinkOpener: ApplinkOpener = ApplinkOpenerDelegate
) : InspirationVideoCarouselListener,
    ApplinkOpener by applinkOpener,
    QueryKeyProvider by queryKeyProvider,
    SearchParameterProvider by searchParameterProvider,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onInspirationVideoCarouselProductImpressed(product: InspirationCarouselDataView.Option.Product) {
        val trackingQueue = trackingQueue ?: return

        val data = createCarouselTrackingUnificationData(product, getSearchParameter())

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

        val data = createCarouselTrackingUnificationData(product, getSearchParameter())
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