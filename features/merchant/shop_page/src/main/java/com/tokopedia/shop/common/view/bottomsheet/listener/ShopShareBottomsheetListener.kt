package com.tokopedia.shop.common.view.bottomsheet.listener

import com.tokopedia.shop.common.view.model.ShopShareModel

interface ShopShareBottomsheetListener {
    fun onItemBottomsheetShareClicked(shopShare: ShopShareModel)
    fun onCloseBottomSheet()
}
