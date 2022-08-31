package com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchContext
import com.tokopedia.autocompletecomponent.util.contextprovider.ContextProvider
import com.tokopedia.autocompletecomponent.util.contextprovider.WeakReferenceContextProvider
import com.tokopedia.iris.Iris
import com.tokopedia.track.TrackApp
import javax.inject.Inject

class CarouselListenerDelegate @Inject constructor(
    @UniversalSearchContext context: Context?,
    private val iris: Iris
): CarouselListener,
    ContextProvider by WeakReferenceContextProvider(context) {

    override val carouselRecycledViewPool: RecyclerView.RecycledViewPool
        get() = RecyclerView.RecycledViewPool()

    override fun onCarouselSeeAllClick(data: CarouselDataView) {
        data.click(TrackApp.getInstance().gtm)
        RouteManager.route(context, data.data.applink)
    }

    override fun onCarouselItemClick(data: CarouselDataView.Product) {
        data.click(TrackApp.getInstance().gtm)
        RouteManager.route(context, data.applink)
    }

    override fun onCarouselItemImpressed(data: CarouselDataView.Product) {
        data.impress(iris)
    }
}