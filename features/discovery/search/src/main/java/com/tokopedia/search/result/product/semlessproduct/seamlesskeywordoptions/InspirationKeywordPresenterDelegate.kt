package com.tokopedia.search.result.product.semlessproduct.seamlesskeywordoptions

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.utils.applinkmodifier.ApplinkModifier
import javax.inject.Inject

@SearchScope
class InspirationKeywordPresenterDelegate @Inject constructor(
    private val inspirationKeywordView: InspirationKeywordView,
    private val applinkModifier: ApplinkModifier
) : InspirationKeywordPresenter {

    override fun onInspirationKeywordItemClick(inspirationKeywordItemDataView: InspirationKeywordDataView) {
        val applink = applinkModifier.modifyApplink(inspirationKeywordItemDataView.applink)
        handleBroadMatchSeeMoreClick(inspirationKeywordItemDataView, applink)
    }

    private fun handleBroadMatchSeeMoreClick(
        broadMatchDataView: InspirationKeywordDataView,
        applink: String
    ) {
        trackClickItemKeywordClick(broadMatchDataView)

        inspirationKeywordView.openLink(applink, broadMatchDataView.url)
    }

    private fun trackClickItemKeywordClick(inspirationKeyword: InspirationKeywordDataView) {
        inspirationKeywordView.trackEventClickItemInspirationKeyword(inspirationKeyword)
    }
}
