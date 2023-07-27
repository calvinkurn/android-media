package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct

interface InspirationProductView {

    fun trackEventInspirationProductClickItem(inspirationProduct: InspirationProductItemDataView)

    fun trackEventImpressionInspirationProductItem(inspirationProduct: InspirationProductItemDataView)

    fun openLink(applink: String, url: String)
}
