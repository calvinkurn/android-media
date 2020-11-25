package com.tokopedia.shop.settings.basicinfo.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.settings.basicinfo.view.adapter.ShopDomainSuggestionAdapterFactory

data class ShopDomainSuggestion(val domain: String): Visitable<ShopDomainSuggestionAdapterFactory> {

    override fun type(typeFactory: ShopDomainSuggestionAdapterFactory): Int {
        return typeFactory.type(this)
    }
}