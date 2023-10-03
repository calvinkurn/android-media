package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions

import android.content.Context
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.iris.Iris
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.decodeQueryParameter
import com.tokopedia.track.TrackApp
import javax.inject.Inject

@SearchScope
class InspirationKeywordViewDelegate @Inject constructor(
    private val iris: Iris,
    @SearchContext
    context: Context,
    queryKeyProvider: QueryKeyProvider
) : InspirationKeywordView,
    ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context),
    QueryKeyProvider by queryKeyProvider {
    override fun trackEventImpressionInspirationKeyword(inspirationKeywordData: InspirationKeywordDataView) {
        inspirationKeywordData.asSearchComponentTracking(queryKey).impress(iris)
    }

    override fun trackEventClickItemInspirationKeyword(inspirationKeywordData: InspirationKeywordDataView) {
        inspirationKeywordData.asSearchComponentTracking(queryKey).click(TrackApp.getInstance().gtm)
    }

    override fun openLink(applink: String, url: String) {
        if (applink.isNotEmpty())
            openApplink(context, applink.decodeQueryParameter())
        else
            openApplink(context, url)
    }

    private fun InspirationKeywordDataView.asSearchComponentTracking(keyword: String): SearchComponentTracking =
        searchComponentTracking(
            trackingOption = trackingOption,
            keyword = keyword,
            valueId = "0",
            valueName = "$carouselTitle - ${this.keyword}",
            componentId = componentId,
            applink = applink,
            dimension90 = dimension90
        )
}
