package com.tokopedia.shop_showcase.shop_showcase_add.presentation.model

import com.tokopedia.shop_showcase.shop_showcase_add.presentation.adapter.ShopShowcaseAddTypeFactory

/**
 * @author by Rafli Syam on 2020-03-04
 */
class AddShowcaseHeader: BaseShowcaseAddModel {
    override fun type(typeFactory: ShopShowcaseAddTypeFactory): Int {
        return typeFactory.type(this)
    }
}