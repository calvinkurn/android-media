package com.tokopedia.deals.ui.pdp.ui.callback

interface DealsPDPCallbacks {
    fun onShowMoreDesc(title: String, text: String)
    fun onShowAllLocation(outlets: List<com.tokopedia.deals.ui.pdp.data.Outlet>)
    fun onSelectQuantityProduct(data: com.tokopedia.deals.ui.pdp.data.ProductDetailData)
    fun onShowShareLoader()
    fun onHideShareLoader()
}
