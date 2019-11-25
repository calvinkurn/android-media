package com.tokopedia.officialstore.official.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.official.data.model.Banner
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

class OfficialBannerViewModel(val banner: MutableList<Banner>, val categoryName: String) : Visitable<OfficialHomeAdapterTypeFactory>, ImpressHolder() {

    override fun type(adapterTypeFactory: OfficialHomeAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

    fun getBannerImgUrl(): List<String> {
        return banner.map {
            it.imageUrl
        }
    }
}