package com.tokopedia.shop.settings.etalase.data

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.settings.etalase.view.adapter.factory.BaseShopEtalaseFactory

/**
 * Created by hendry on 20/08/18.
 */
class ShopEtalaseUiModel : BaseShopEtalaseUiModel {
    constructor() : super() {}

    constructor(shopEtalaseModel: ShopEtalaseModel, isPrimaryEtalase: Boolean) : super(shopEtalaseModel, isPrimaryEtalase) {}

    override fun type(typeFactory: BaseShopEtalaseFactory): Int {
        return typeFactory.type(this)
    }

    constructor(`in`: Parcel) : super(`in`) {}

    companion object CREATOR : Parcelable.Creator<BaseShopEtalaseUiModel> {
        override fun createFromParcel(source: Parcel): BaseShopEtalaseUiModel {
            return ShopEtalaseUiModel(source)
        }

        override fun newArray(size: Int): Array<BaseShopEtalaseUiModel?> {
            return arrayOfNulls(size)
        }
    }

}
