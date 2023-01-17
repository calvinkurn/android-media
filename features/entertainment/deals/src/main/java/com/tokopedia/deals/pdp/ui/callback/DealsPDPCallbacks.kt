package com.tokopedia.deals.pdp.ui.callback

import com.tokopedia.deals.pdp.data.Outlet
import com.tokopedia.deals.pdp.data.ProductDetailData

interface DealsPDPCallbacks {
    fun onShowMoreDesc(title: String, text: String)
    fun onShowAllLocation(outlets: List<Outlet>)
    fun onSelectQuantityProduct(data: ProductDetailData)
    fun onShowShareLoader()
    fun onHideShareLoader()
}
