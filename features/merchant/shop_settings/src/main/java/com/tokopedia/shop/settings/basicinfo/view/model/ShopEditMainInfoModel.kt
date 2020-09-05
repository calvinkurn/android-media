package com.tokopedia.shop.settings.basicinfo.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.settings.basicinfo.view.adapter.ShopEditBasicInfoAdapterFactory

data class ShopEditMainInfoModel (
    var name: String = "",
    var domain: String = "",
    var tagLine: String = "",
    var description: String = "",
    var isNameAllowed: Boolean = false,
    var isDomainAllowed: Boolean = false,
    var shopDomains: List<String> = emptyList(),
    var nameErrorMessage: String = "",
    var domainErrorMessage: String = "",
    var nameNotValid: Boolean = false,
    var domainNotValid: Boolean = false
): Visitable<ShopEditBasicInfoAdapterFactory> {
    override fun type(typeFactory: ShopEditBasicInfoAdapterFactory): Int = typeFactory.type(this)
}