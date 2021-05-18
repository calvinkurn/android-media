package com.tokopedia.minicart.ui.widget

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData

interface MiniCartWidgetListener {

    /*
    * Callback to notify holder activity / fragment that there's newly fetched cart data.
    * Holder activity / fragment need to update product card state based on provided data.
    * */
    fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData)

}