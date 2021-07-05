package com.tokopedia.shop.settings.basicinfo.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.shop.settings.basicinfo.view.model.ShopDomainSuggestion

interface ShopDomainSuggestionAdapterFactory: AdapterTypeFactory {

    fun type(viewModel: ShopDomainSuggestion): Int
}