package com.tokopedia.search.result.product.videowidget

import android.content.Context
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTracking
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
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

        InspirationCarouselTracking.trackCarouselImpression(trackingQueue, data)
    }

    override fun onInspirationVideoCarouselProductClicked(videoItem: VideoCarouselDataView.VideoItem) {
        openApplink(context, videoItem.applink)

        val data = createCarouselTrackingUnificationData(
            videoItem.toProductModel(),
            getSearchParameter()
        )

        InspirationCarouselTracking.trackCarouselClick(data)
    }
}
