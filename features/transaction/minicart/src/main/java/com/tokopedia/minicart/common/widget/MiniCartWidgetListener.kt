package com.tokopedia.minicart.common.widget

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData

interface MiniCartWidgetListener {

    /*
    * Callback to notify holder fragment that there's newly fetched cart data.
    * Holder fragment need to update product card state based on provided data.
    * */
    fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData)

}