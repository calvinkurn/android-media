package com.tokopedia.search.result.product.semlessproduct.seamlesskeywordoptions

interface InspirationKeywordView {

    fun trackEventImpressionInspirationKeyword(inspirationKeywordData: InspirationKeywordDataView)

    fun trackEventClickItemInspirationKeyword(inspirationKeywordData: InspirationKeywordDataView)

    fun openLink(applink: String, url: String)
}
