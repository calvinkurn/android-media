package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.official.data.model.Shop
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory

class OfficialFeaturedShopViewModel(val featuredShop: MutableList<Shop>) : Visitable<OfficialHomeAdapterTypeFactory> {

    override fun type(adapterTypeFactory: OfficialHomeAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }
}