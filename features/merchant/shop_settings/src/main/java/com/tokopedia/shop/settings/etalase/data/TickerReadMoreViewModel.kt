package com.tokopedia.shop.settings.etalase.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.settings.etalase.view.adapter.factory.BaseShopEtalaseFactory

data class TickerReadMoreViewModel(
        val tickerTitle: String,
        val tickerDescription: String,
        val readMoreString: String
): Visitable<BaseShopEtalaseFactory> {
    override fun type(typeFactory: BaseShopEtalaseFactory): Int {
        return typeFactory.type(this)
    }
}