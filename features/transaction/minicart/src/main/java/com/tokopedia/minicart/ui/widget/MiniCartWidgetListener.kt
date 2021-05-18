package com.tokopedia.minicart.ui.widget

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData

interface MiniCartWidgetListener {

    fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData)

}