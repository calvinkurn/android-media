package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.official.data.model.HeaderShop
import com.tokopedia.officialstore.official.data.model.Shop
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory

class OfficialFeaturedShopViewModel(
        val featuredShop: MutableList<Shop>,
        val headerShop: HeaderShop?,
        val categoryName: String,
        val listener: FeaturedShopListener
) : Visitable<OfficialHomeAdapterTypeFactory> {

    override fun type(adapterTypeFactory: OfficialHomeAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }
}