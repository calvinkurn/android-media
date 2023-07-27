package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions

interface InspirationKeywordListener {

    fun onInspirationKeywordImpressed(inspirationKeywordData: InspirationKeywordDataView)

    fun onInspirationKeywordItemClicked(inspirationKeywordData: InspirationKeywordDataView)
}
