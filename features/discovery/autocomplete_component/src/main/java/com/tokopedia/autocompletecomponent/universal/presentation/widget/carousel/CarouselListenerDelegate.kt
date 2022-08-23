package com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocompletecomponent.util.contextprovider.ContextProvider
import com.tokopedia.autocompletecomponent.util.contextprovider.WeakReferenceContextProvider
import javax.inject.Inject

class CarouselListenerDelegate @Inject constructor(
    @ApplicationContext context: Context?,
): CarouselListener,
    ContextProvider by WeakReferenceContextProvider(context) {

    override val carouselRecycledViewPool: RecyclerView.RecycledViewPool
        get() = RecyclerView.RecycledViewPool()

    override fun onCarouselSeeAllClick(data: CarouselDataView) {
        RouteManager.route(context, data.applink)
    }

    override fun onCarouselItemClick(data: CarouselDataView.Product) {
        RouteManager.route(context, data.applink)
    }

    override fun onCarouselItemImpressed(data: CarouselDataView.Product) {

    }
}