package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.utils.applinkmodifier.ApplinkModifier
import javax.inject.Inject

@SearchScope
class InspirationKeywordPresenterDelegate @Inject constructor(
    private val inspirationKeywordView: InspirationKeywordView,
    private val applinkModifier: ApplinkModifier
) : InspirationKeywordPresenter {
    override fun onInspirationKeywordImpressed(inspirationKeywordData: InspirationKeywordDataView) {
        inspirationKeywordView.trackEventImpressionInspirationKeyword(inspirationKeywordData)
    }

    override fun onInspirationKeywordItemClick(inspirationKeywordItemDataView: InspirationKeywordDataView) {
        val applink = applinkModifier.modifyApplink(inspirationKeywordItemDataView.applink)
        handleInspirationKeywordItemClick(inspirationKeywordItemDataView, applink)
    }

    private fun handleInspirationKeywordItemClick(
        inspirationKeyword: InspirationKeywordDataView,
        applink: String
    ) {
        trackClickItemKeywordClick(inspirationKeyword)
        inspirationKeywordView.openLink(applink, inspirationKeyword.url)
    }

    private fun trackClickItemKeywordClick(inspirationKeyword: InspirationKeywordDataView) {
        inspirationKeywordView.trackEventClickItemInspirationKeyword(inspirationKeyword)
    }
}
