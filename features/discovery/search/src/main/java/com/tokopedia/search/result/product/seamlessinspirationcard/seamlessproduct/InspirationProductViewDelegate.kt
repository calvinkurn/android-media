package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct

import android.content.Context
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTracking
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.decodeQueryParameter
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

@SearchScope
class InspirationProductViewDelegate @Inject constructor(
    private val trackingQueue: TrackingQueue,
    searchParameterProvider: SearchParameterProvider,
    @SearchContext
    context: Context,
    queryKeyProvider: QueryKeyProvider,
): InspirationProductView,
    ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context),
    SearchParameterProvider by searchParameterProvider,
    QueryKeyProvider by queryKeyProvider {

    override fun trackInspirationProductSeamlessImpression(
        type: String,
        product: InspirationCarouselDataView.Option.Product,
    ) {
        val trackingQueue = trackingQueue
        val data = createCarouselTrackingUnificationData(
            product,
            getSearchParameter()
        )

        InspirationCarouselTracking.trackCarouselImpression(trackingQueue, data)
    }

    override fun trackInspirationProductSeamlessClick(
        type: String,
        product: InspirationCarouselDataView.Option.Product,
    ) {
        val data = createCarouselTrackingUnificationData(
            product,
            getSearchParameter()
        )

        InspirationCarouselTracking.trackCarouselClick(data)
    }

    override fun openLink(applink: String, url: String) {
        if (applink.isNotEmpty())
            openApplink(context, applink.decodeQueryParameter())
        else
            openApplink(context, url)
    }
}
