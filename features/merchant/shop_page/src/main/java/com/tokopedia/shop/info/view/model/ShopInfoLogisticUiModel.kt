package com.tokopedia.shop.info.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticTypeFactory

class ShopInfoLogisticUiModel : Visitable<ShopInfoLogisticTypeFactory?> {
    var shipmentImage: String? = null
    var shipmentName: String? = null
    var shipmentPackage: String? = null
    override fun type(typeFactory: ShopInfoLogisticTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}