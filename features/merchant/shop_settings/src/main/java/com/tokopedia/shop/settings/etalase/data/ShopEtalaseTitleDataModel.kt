package com.tokopedia.shop.settings.etalase.data

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.shop.settings.etalase.view.adapter.factory.BaseShopEtalaseFactory

/**
 * Created by hendry on 20/08/18.
 */
class ShopEtalaseTitleDataModel : BaseShopEtalaseDataModel {
    constructor(nameTo: String) : super() {
        name = nameTo
    }

    constructor(`in`: Parcel) : super(`in`) {}

    override fun type(typeFactory: BaseShopEtalaseFactory): Int {
        return typeFactory.type(this)
    }

    companion object CREATOR : Parcelable.Creator<BaseShopEtalaseDataModel> {
        override fun createFromParcel(source: Parcel): BaseShopEtalaseDataModel {
            return ShopEtalaseTitleDataModel(source)
        }

        override fun newArray(size: Int): Array<BaseShopEtalaseDataModel?> {
            return arrayOfNulls(size)
        }
    }

}
