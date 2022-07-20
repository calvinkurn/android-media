package com.tokopedia.search.result.shop.chooseaddress

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.shop.presentation.typefactory.ShopListTypeFactory

internal class ChooseAddressDataView: Visitable<ShopListTypeFactory> {
    override fun type(typeFactory: ShopListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}