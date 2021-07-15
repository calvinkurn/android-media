package com.tokopedia.hotel.search_map.data.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.hotel.search_map.presentation.adapter.PropertyAdapterTypeFactory

data class HotelLoadingModel (
        var isForHorizontalItem: Boolean = false) : Visitable<PropertyAdapterTypeFactory>{
    override fun type(typeFactory: PropertyAdapterTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}