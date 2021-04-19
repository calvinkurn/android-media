package com.tokopedia.hotel.search_map.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.hotel.search.presentation.adapter.PropertyAdapterTypeFactory

data class HotelLoadingModel (
        var isForHorizontalItem: Boolean = false) : Visitable<PropertyAdapterTypeFactory>{
    override fun type(typeFactory: PropertyAdapterTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}