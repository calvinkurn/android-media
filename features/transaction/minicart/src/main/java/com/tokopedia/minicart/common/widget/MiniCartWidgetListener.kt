package com.tokopedia.minicart.common.widget

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData

interface MiniCartWidgetListener {

    /*
    * Callback to notify holder fragment that there's update, whether it's a newly fetched cart data,
    * or there's changes on cart items on cart list bottom sheet.
    * Holder fragment need to update product card state based on provided data, especially quantity editor / `+ Keranjang` button.
    * Holder fragment need to hide / set MiniCartWidget to gone as well if value `miniCartSimplifiedData.isShowMiniCartWidget` is false.
    * */
    fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData)

}