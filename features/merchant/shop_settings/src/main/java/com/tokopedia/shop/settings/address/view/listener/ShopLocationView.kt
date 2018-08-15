package com.tokopedia.shop.settings.address.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.shop.settings.address.data.ShopLocationViewModel

interface ShopLocationView: CustomerView {
    fun onSuccessLoadAddresses(addresses: List<ShopLocationViewModel>?)
    fun onErrorLoadAddresses(throwable: Throwable?)
    fun onSuccessDeleteAddress(string: String?)
    fun onErrorDeleteAddress(throwable: Throwable?)
}