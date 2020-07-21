package com.tokopedia.shop.settings.etalase.data

import com.tokopedia.shop.settings.etalase.view.adapter.factory.BaseShopEtalaseFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
class ShopEtalaseTitleViewModel(var nameTo: String) : BaseShopEtalaseViewModel(name = nameTo) {

    override fun type(typeFactory: BaseShopEtalaseFactory): Int {
        return typeFactory.type(this)
    }

}
