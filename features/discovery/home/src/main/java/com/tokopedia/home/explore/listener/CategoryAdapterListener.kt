package com.tokopedia.home.explore.listener


import com.tokopedia.home.explore.domain.model.LayoutRows

/**
 * Created by errysuprayogi on 1/30/18.
 */

interface CategoryAdapterListener {
    fun onMarketPlaceItemClicked(data: LayoutRows)

    fun onDigitalItemClicked(data: LayoutRows)

    fun onGimickItemClicked(data: LayoutRows)

    fun onApplinkClicked(data: LayoutRows)

    fun openShop()

    fun onDigitalMoreClicked()

    fun showNetworkError(string: String)

    fun openShopSetting()

    fun trackingItemGridClick(data: LayoutRows)
}
