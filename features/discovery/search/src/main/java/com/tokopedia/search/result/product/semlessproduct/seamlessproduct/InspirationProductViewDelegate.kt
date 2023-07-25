package com.tokopedia.search.result.product.semlessproduct.seamlessproduct

import android.content.Context
import com.tokopedia.iris.Iris
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.broadmatch.BroadMatchTracking
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.decodeQueryParameter
import com.tokopedia.search.utils.getUserId
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@SearchScope
class InspirationProductViewDelegate @Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface,
    @SearchContext
    context: Context,
    queryKeyProvider: QueryKeyProvider,
) : InspirationProductView,
    ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context),
    QueryKeyProvider by queryKeyProvider {
    override fun trackEventInspirationProductClickItem(inspirationProduct: InspirationProductItemDataView) {
        val inspirationProductItem = ArrayList<Any>()
        inspirationProductItem.add(inspirationProduct.asClickObjectDataLayer())

        BroadMatchTracking.trackEventClickBroadMatchItem(
            queryKey,
            inspirationProduct.alternativeKeyword,
            getUserId(userSession),
            inspirationProduct.isOrganicAds,
            inspirationProduct.componentId,
            inspirationProductItem,
        )
    }

    override fun trackEventImpressionInspirationProductItem(inspirationProduct: InspirationProductItemDataView) {
        val trackingQueue = trackingQueue
        val broadMatchItemAsObjectDataLayer = ArrayList<Any>()
        broadMatchItemAsObjectDataLayer.add(inspirationProduct.asImpressionObjectDataLayer())

        BroadMatchTracking.trackEventImpressionBroadMatch(
            trackingQueue,
            queryKey,
            inspirationProduct.alternativeKeyword,
            getUserId(userSession),
            broadMatchItemAsObjectDataLayer,
        )
    }

    override fun openLink(applink: String, url: String) {
        if (applink.isNotEmpty())
            openApplink(context, applink.decodeQueryParameter())
        else
            openApplink(context, url)
    }
}
