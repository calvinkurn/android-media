package com.tokopedia.search.result.product.suggestion

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.iris.Iris
import com.tokopedia.search.utils.applinkmodifier.ApplinkModifier
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.track.TrackApp

class SuggestionListenerDelegate(
    private val iris: Iris,
    private val applinkModifier: ApplinkModifier,
    context: Context?,
): SuggestionListener,
    ContextProvider by WeakReferenceContextProvider(context),
    ApplinkOpener by ApplinkOpenerDelegate {

    override fun onSuggestionImpressed(suggestionDataView: SuggestionDataView) {
        suggestionDataView.impress(iris)
    }

    override fun onSuggestionClicked(suggestionDataView: SuggestionDataView) {
        suggestionDataView.click(TrackApp.getInstance().gtm)
        performNewProductSearch(suggestionDataView.suggestedQuery)
    }

    private fun performNewProductSearch(queryParams: String) {
        val applinkToSearchResult = ApplinkConstInternalDiscovery.SEARCH_RESULT + "?" + queryParams
        val modifiedApplinkToSearchResult = applinkModifier.modifyApplink(applinkToSearchResult)

        openApplink(context, modifiedApplinkToSearchResult)
    }
}
