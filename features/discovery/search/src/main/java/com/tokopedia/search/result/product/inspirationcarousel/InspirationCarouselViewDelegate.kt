package com.tokopedia.search.result.product.inspirationcarousel

import android.content.Context
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.result.presentation.view.fragment.RecyclerViewUpdater
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTracking
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.decodeQueryParameter
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

class InspirationCarouselViewDelegate @Inject constructor(
    queryKeyProvider: QueryKeyProvider,
    searchParameterProvider: SearchParameterProvider,
    @SearchContext
    context: Context,
    private val trackingQueue: TrackingQueue,
) : InspirationCarouselView,
    QueryKeyProvider by queryKeyProvider,
    SearchParameterProvider by searchParameterProvider,
    ContextProvider by WeakReferenceContextProvider(context),
    ApplinkOpener by ApplinkOpenerDelegate {

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

    override fun openLink(applink: String, url: String) {
        if (applink.isNotEmpty())
            openApplink(context, applink.decodeQueryParameter())
        else
            openApplink(context, url)
    }
}
