package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct

interface InspirationProductPresenter {

    fun onInspirationProductItemImpressed(inspirationProductData: InspirationProductItemDataView)

    fun onInspirationProductItemClick(inspirationProductData: InspirationProductItemDataView)
}
