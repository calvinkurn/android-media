package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchAdapterTypeFactory

class BrandlistSearchHeaderUiModel(
        val headerText: String,
        val totalBrand: Int?
) : Visitable<BrandlistSearchAdapterTypeFactory> {

    override fun type(adapterTypeFactory: BrandlistSearchAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

    companion object {
        const val RECOMMENDATION_HEADER = "Rekomendasi untuk kamu"
        const val TOTAL_BRANDS_HEADER = "Semua Brand"
    }
}