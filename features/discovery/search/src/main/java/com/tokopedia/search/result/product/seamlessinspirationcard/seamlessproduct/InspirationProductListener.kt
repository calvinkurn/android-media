package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct

interface InspirationProductListener {
    fun onInspirationProductItemImpressed(inspirationProductData: InspirationProductItemDataView)

    fun onInspirationProductItemClicked(inspirationProductData: InspirationProductItemDataView)

    fun onInspirationProductItemThreeDotsClicked(inspirationProductData: InspirationProductItemDataView)
}
