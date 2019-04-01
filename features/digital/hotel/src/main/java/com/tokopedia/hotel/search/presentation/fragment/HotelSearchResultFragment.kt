package com.tokopedia.hotel.search.presentation.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.search.data.Property
import com.tokopedia.hotel.search.presentation.adapter.PropertyAdapterTypeFactory

class HotelSearchResultFragment: BaseListFragment<Property, PropertyAdapterTypeFactory>() {

    override fun getAdapterTypeFactory(): PropertyAdapterTypeFactory = PropertyAdapterTypeFactory()

    override fun onItemClicked(t: Property?) {}

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(HotelComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {}
}