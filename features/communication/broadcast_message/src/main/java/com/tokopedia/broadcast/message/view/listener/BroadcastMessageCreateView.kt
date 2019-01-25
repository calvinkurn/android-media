package com.tokopedia.broadcast.message.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo

interface BroadcastMessageCreateView: CustomerView {
    fun onErrorGetShopInfo(e: Throwable)
    fun onSuccessGetShopInfo(shopInfo: ShopInfo)

}