package com.tokopedia.search.result.product.semlessproduct.seamlessproduct

interface InspirationProductListener {
    fun onInspirationProductItemImpressed(inspirationProductData: InspirationProductItemDataView)

    fun onInspirationProductItemClicked(inspirationProductData: InspirationProductItemDataView)

    fun onInspirationProductItemThreeDotsClicked(inspirationProductData: InspirationProductItemDataView)
}
