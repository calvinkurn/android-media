package com.tokopedia.shop.settings.address.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationUiModel

interface ShopLocationOldView: CustomerView {
    fun onSuccessLoadAddresses(addresses: List<ShopLocationUiModel>?)
    fun onErrorLoadAddresses(throwable: Throwable?)
    fun onSuccessDeleteAddress(string: String?)
    fun onErrorDeleteAddress(throwable: Throwable?)
}