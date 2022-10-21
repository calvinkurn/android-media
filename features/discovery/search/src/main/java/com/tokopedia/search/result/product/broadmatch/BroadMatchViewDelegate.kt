package com.tokopedia.search.result.product.broadmatch

import android.content.Context
import com.tokopedia.iris.Iris
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.QueryKeyProvider
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
class BroadMatchViewDelegate @Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val iris: Iris,
    private val userSession: UserSessionInterface,
    @SearchContext
    context: Context,
    queryKeyProvider: QueryKeyProvider,
) : BroadMatchView,
    ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context),
    QueryKeyProvider by queryKeyProvider {

    override fun trackEventClickBroadMatchItem(broadMatchItemDataView: BroadMatchItemDataView) {
        val broadMatchItem = ArrayList<Any>()
        broadMatchItem.add(broadMatchItemDataView.asClickObjectDataLayer())

        BroadMatchTracking.trackEventClickBroadMatchItem(
            queryKey,
            broadMatchItemDataView.alternativeKeyword,
            getUserId(userSession),
            broadMatchItemDataView.isOrganicAds,
            broadMatchItemDataView.componentId,
            broadMatchItem,
        )
    }

    override fun trackEventImpressionBroadMatchItem(broadMatchItemDataView: BroadMatchItemDataView) {
        val trackingQueue = trackingQueue
        val broadMatchItemAsObjectDataLayer = ArrayList<Any>()
        broadMatchItemAsObjectDataLayer.add(broadMatchItemDataView.asImpressionObjectDataLayer())

        BroadMatchTracking.trackEventImpressionBroadMatch(
            trackingQueue,
            queryKey,
            broadMatchItemDataView.alternativeKeyword,
            getUserId(userSession),
            broadMatchItemAsObjectDataLayer,
        )
    }

    override fun trackEventImpressionBroadMatch(broadMatchDataView: BroadMatchDataView) {
        BroadMatchTracking.trackEventImpressionBroadMatch(
            iris,
            broadMatchDataView,
        )
    }

    override fun trackEventClickSeeMoreBroadMatch(broadMatchDataView: BroadMatchDataView) {
        BroadMatchTracking.trackEventClickBroadMatchSeeMore(
            broadMatchDataView,
            queryKey,
            broadMatchDataView.keyword,
            broadMatchDataView.dimension90,
        )
    }

    override fun openLink(applink: String, url: String) {
        if (applink.isNotEmpty())
            openApplink(context, applink.decodeQueryParameter())
        else
            openApplink(context, url)
    }
}
