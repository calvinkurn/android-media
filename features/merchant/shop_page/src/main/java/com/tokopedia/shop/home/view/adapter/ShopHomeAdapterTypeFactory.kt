package com.tokopedia.shop.home.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.shop.home.view.model.BannerUIModel
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductViewModel

class ShopHomeAdapterTypeFactory: BaseAdapterTypeFactory() {
    fun type(bannerUIModel: BannerUIModel): Int {
        return -1
    }

    fun type(shopProductViewModel: ShopProductViewModel): Int {
        return -1
    }
}