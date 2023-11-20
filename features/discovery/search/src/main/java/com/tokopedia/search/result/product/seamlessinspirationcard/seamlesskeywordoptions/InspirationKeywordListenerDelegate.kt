package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions

import com.tokopedia.search.utils.FragmentProvider

class InspirationKeywordListenerDelegate(
    private val presenter: InspirationKeywordPresenter?,
    fragmentProvider: FragmentProvider
) : InspirationKeywordListener,
    FragmentProvider by fragmentProvider {
    override fun onInspirationKeywordImpressed(inspirationKeywordData: InspirationKeywordDataView) {
        presenter?.onInspirationKeywordImpressed(inspirationKeywordData)
    }

    override fun onInspirationKeywordItemClicked(inspirationKeywordData: InspirationKeywordDataView) {
        presenter?.onInspirationKeywordItemClick(inspirationKeywordData)
    }
}
