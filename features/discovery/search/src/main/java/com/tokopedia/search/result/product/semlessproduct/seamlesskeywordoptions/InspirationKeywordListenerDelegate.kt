package com.tokopedia.search.result.product.semlessproduct.seamlesskeywordoptions

import com.tokopedia.search.utils.FragmentProvider

class InspirationKeywordListenerDelegate(
    private val presenter: InspirationKeywordPresenter?,
    fragmentProvider: FragmentProvider,
) : InspirationKeywordListener,
    FragmentProvider by fragmentProvider {

    override fun onBroadMatchItemClicked(broadMatchItemDataView: InspirationKeywordDataView) {
        presenter?.onInspirationKeywordItemClick(broadMatchItemDataView)
    }
}
