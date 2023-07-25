package com.tokopedia.search.result.product.semlessproduct.seamlesskeywordoptions

import android.content.Context
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.broadmatch.BroadMatchTracking
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.search.utils.decodeQueryParameter
import javax.inject.Inject

@SearchScope
class InspirationKeywordViewDelegate @Inject constructor(
    @SearchContext
    context: Context,
    queryKeyProvider: QueryKeyProvider,
) : InspirationKeywordView,
    ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context),
    QueryKeyProvider by queryKeyProvider {

    override fun trackEventClickItemInspirationKeyword(broadMatchDataView: InspirationKeywordDataView) {
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
