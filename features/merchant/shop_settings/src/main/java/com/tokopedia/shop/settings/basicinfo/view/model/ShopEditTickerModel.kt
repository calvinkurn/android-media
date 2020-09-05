package com.tokopedia.shop.settings.basicinfo.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.settings.basicinfo.view.adapter.ShopEditBasicInfoAdapterFactory

class ShopEditTickerModel(
        var isNameAllowed: Boolean = false,
        var isDomainAllowed: Boolean = false,
        var reasonNameNotAllowed: String = "",
        var reasonDomainNotAllowed: String = ""
): Visitable<ShopEditBasicInfoAdapterFactory> {
    override fun type(typeFactory: ShopEditBasicInfoAdapterFactory): Int = typeFactory.type(this)
}