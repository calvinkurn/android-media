package com.tokopedia.shop.settings.etalase.data

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.settings.etalase.view.adapter.factory.BaseShopEtalaseFactory

/**
 * Created by hendry on 20/08/18.
 */
class ShopEtalaseViewModel : BaseShopEtalaseViewModel {
    constructor() : super() {}

    constructor(shopEtalaseModel: ShopEtalaseModel, isPrimaryEtalase: Boolean) : super(shopEtalaseModel, isPrimaryEtalase) {}

    override fun type(typeFactory: BaseShopEtalaseFactory): Int {
        return typeFactory.type(this)
    }

    constructor(`in`: Parcel) : super(`in`) {}

    companion object CREATOR : Parcelable.Creator<BaseShopEtalaseViewModel> {
        override fun createFromParcel(source: Parcel): BaseShopEtalaseViewModel {
            return ShopEtalaseViewModel(source)
        }

        override fun newArray(size: Int): Array<BaseShopEtalaseViewModel?> {
            return arrayOfNulls(size)
        }
    }

}
