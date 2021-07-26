package com.tokopedia.shop.info.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo

interface OldShopInfoView : CustomerView {
    fun onSuccessGetShopInfo(shopInfo: ShopInfo?)
    fun onErrorGetShopInfo(e: Throwable?)
}