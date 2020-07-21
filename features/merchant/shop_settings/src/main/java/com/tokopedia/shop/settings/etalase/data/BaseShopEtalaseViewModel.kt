package com.tokopedia.shop.settings.etalase.data

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.settings.etalase.view.adapter.factory.BaseShopEtalaseFactory

/**
 * Created by hendry on 23/08/18.
 */
abstract class BaseShopEtalaseViewModel(
        var id: String = "",
        var name: String = "",
        var count: Int = 0,
        @ShopEtalaseTypeDef
        var type: Int = 0,
        var isPrimaryEtalase: Boolean = false
) : Parcelable, Visitable<BaseShopEtalaseFactory> {

    constructor(shopEtalaseModel: ShopEtalaseModel,
                isPrimaryEtalase: Boolean) : this() {
        this.id = shopEtalaseModel.id
        this.name = shopEtalaseModel.name
        this.count = shopEtalaseModel.count
        this.type = shopEtalaseModel.type
        this.isPrimaryEtalase = isPrimaryEtalase
    }

    abstract override fun type(typeFactory: BaseShopEtalaseFactory): Int


}
