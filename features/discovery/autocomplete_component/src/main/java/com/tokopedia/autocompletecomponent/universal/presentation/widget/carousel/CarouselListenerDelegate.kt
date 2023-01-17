package com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocompletecomponent.universal.analytics.CarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchContext
import com.tokopedia.autocompletecomponent.util.contextprovider.ContextProvider
import com.tokopedia.autocompletecomponent.util.contextprovider.WeakReferenceContextProvider
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

class CarouselListenerDelegate @Inject constructor(
    @UniversalSearchContext context: Context?,
    private val trackingQueue: TrackingQueue,
    private val searchParameter : SearchParameter,
    private val carouselTrackingUnification: CarouselTrackingUnification,
): CarouselListener,
    ContextProvider by WeakReferenceContextProvider(context) {

    override val carouselRecycledViewPool: RecyclerView.RecycledViewPool
        get() = RecyclerView.RecycledViewPool()

    override fun onCarouselSeeAllClick(data: CarouselDataView) {
        carouselTrackingUnification.trackCarouselClickSeeAll(
            searchParameter.getSearchQuery(),
            data.data
        )
        RouteManager.route(context, data.data.applink)
    }

    override fun onCarouselItemClick(data: CarouselDataView.Product) {
        val trackingData = createCarouselTrackingUnificationData(data, searchParameter)
        carouselTrackingUnification.trackCarouselClick(trackingData)
        RouteManager.route(context, data.applink)
    }

    override fun onCarouselItemImpressed(data: CarouselDataView.Product) {
        val trackingData = createCarouselTrackingUnificationData(data, searchParameter)
        carouselTrackingUnification.trackCarouselImpression(trackingQueue, trackingData)
    }
}