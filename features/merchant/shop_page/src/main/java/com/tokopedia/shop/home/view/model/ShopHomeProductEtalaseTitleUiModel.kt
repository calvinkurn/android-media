package com.tokopedia.shop.home.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory

/**
 * Created by zulfikarrahman on 1/16/18.
 */

data class ShopHomeProductEtalaseTitleUiModel(
    var etalaseName: String,
    var etalaseBadge: String
) : Visitable<ShopHomeAdapterTypeFactory> {

    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
