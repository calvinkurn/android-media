package com.tokopedia.shop.settings.etalase.data

import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.settings.etalase.view.adapter.factory.BaseShopEtalaseFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
class ShopEtalaseViewModel(val shopEtalaseModel: ShopEtalaseModel, val primaryEtalase: Boolean) : BaseShopEtalaseViewModel(shopEtalaseModel, primaryEtalase) {

    override fun type(typeFactory: BaseShopEtalaseFactory): Int {
        return typeFactory.type(this)
    }

}
