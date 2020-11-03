package com.tokopedia.shop.settings.etalase.data

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.settings.etalase.view.adapter.factory.BaseShopEtalaseFactory

/**
 * Created by hendry on 20/08/18.
 */
class ShopEtalaseDataModel : BaseShopEtalaseDataModel {
    constructor() : super() {}

    constructor(shopEtalaseModel: ShopEtalaseModel, isPrimaryEtalase: Boolean) : super(shopEtalaseModel, isPrimaryEtalase) {}

    override fun type(typeFactory: BaseShopEtalaseFactory): Int {
        return typeFactory.type(this)
    }

    constructor(`in`: Parcel) : super(`in`) {}

    companion object CREATOR : Parcelable.Creator<BaseShopEtalaseDataModel> {
        override fun createFromParcel(source: Parcel): BaseShopEtalaseDataModel {
            return ShopEtalaseDataModel(source)
        }

        override fun newArray(size: Int): Array<BaseShopEtalaseDataModel?> {
            return arrayOfNulls(size)
        }
    }

}
