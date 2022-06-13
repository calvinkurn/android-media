package com.tokopedia.search.result.product.videowidget

import android.content.Context
import com.tokopedia.search.analytics.InspirationCarouselTrackingUnification
import com.tokopedia.search.analytics.InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.product.querykeyprovider.QueryKeyProvider
import com.tokopedia.search.result.product.searchparameterprovider.SearchParameterProvider
import com.tokopedia.search.result.product.videowidget.VideoCarouselDataViewMapper.toProductModel
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.video_widget.carousel.InspirationVideoCarouselListener
import com.tokopedia.video_widget.carousel.VideoCarouselDataView

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

    override fun onInspirationVideoCarouselProductImpressed(videoItem: VideoCarouselDataView.VideoItem) {
        val trackingQueue = trackingQueue ?: return

        val data = createCarouselTrackingUnificationData(
            videoItem.toProductModel(),
            getSearchParameter()
        )

        inspirationCarouselTrackingUnification.trackCarouselImpression(trackingQueue, data) {
            val products = ArrayList<Any>()
            products.add(videoItem.getInspirationCarouselListProductImpressionAsObjectDataLayer())

            SearchTracking.trackImpressionInspirationCarouselList(
                trackingQueue,
                videoItem.inspirationCarouselType,
                queryKey,
                products
            )
        }
    }

    override fun onInspirationVideoCarouselProductClicked(videoItem: VideoCarouselDataView.VideoItem) {
        openApplink(context, videoItem.applink)

        val data = createCarouselTrackingUnificationData(
            videoItem.toProductModel(),
            getSearchParameter()
        )

        inspirationCarouselTrackingUnification.trackCarouselClick(data) {
            val products = ArrayList<Any>()
            products.add(videoItem.getInspirationCarouselListProductAsObjectDataLayer())

            SearchTracking.trackEventClickInspirationCarouselListProduct(
                videoItem.inspirationCarouselType,
                queryKey,
                products,
            )
        }
    }
}