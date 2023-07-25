package com.tokopedia.search.result.product.semlessproduct.seamlessproduct

interface InspirationProductListener {
    fun onInspirationProductItemImpressed(broadMatchItemDataView: InspirationProductItemDataView)

    fun onInspirationProductItemClicked(broadMatchItemDataView: InspirationProductItemDataView)

    fun onInspirationProductItemThreeDotsClicked(broadMatchItemDataView: InspirationProductItemDataView)
}
