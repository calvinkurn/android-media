package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.adapter.BrandlistHeaderBrandInterface
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchAdapterTypeFactory

class BrandlistSearchAllBrandGroupHeaderUiModel(
        val listener: BrandlistHeaderBrandInterface,
        val totalBrands: Int,
        val selectedChip: Int,
        val lastTimeChipIsClicked: Long,
        val recyclerViewLastState: Parcelable?
) : Visitable<BrandlistSearchAdapterTypeFactory> {

    var recyclerViewState: Parcelable? = null

    override fun type(adapterTypeFactory: BrandlistSearchAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }
}