package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageTypeFactory
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.adapter.BrandlistHeaderBrandInterface

class AllBrandGroupHeaderUiModel(
        val listener: BrandlistHeaderBrandInterface,
        val totalBrands: Int,
        val selectedChip: Int,
        val lastTimeChipsClicked: Long,
        val recyclerViewLastState: Parcelable?
) : Visitable<BrandlistPageTypeFactory> {

    var recyclerViewState: Parcelable? = null

    override fun type(adapterTypeFactory: BrandlistPageTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }
}