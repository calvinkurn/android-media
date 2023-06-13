package com.tokopedia.deals.checkout.ui

import com.tokopedia.deals.pdp.data.Outlet

interface DealsCheckoutCallbacks {
    fun onShowAllLocation(outlets: List<Outlet>)
}
