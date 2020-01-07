package com.tokopedia.shop.newproduct.view.datamodel

import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef.ETALASE_DEFAULT
import com.tokopedia.shop.newproduct.view.adapter.ShopProductEtalaseAdapterTypeFactory

/**
 * Created by normansyahputa on 2/28/18.
 */

data class ShopProductEtalaseChipItemViewModel(
        val etalaseId: String = "",
        val etalaseName: String = "",
        @ShopEtalaseTypeDef private var type: Int = ETALASE_DEFAULT,
        val etalaseBadge: String = "",
        val etalaseCount: Long = 0,
        val highlighted: Boolean = false
) : BaseShopProductEtalaseViewModel {
    override fun type(typeFactory: ShopProductEtalaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
