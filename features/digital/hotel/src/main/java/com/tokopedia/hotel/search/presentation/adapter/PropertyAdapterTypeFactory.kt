package com.tokopedia.hotel.search.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.hotel.search.data.Property

class PropertyAdapterTypeFactory: BaseAdapterTypeFactory() {

    fun type(data: Property): Int = 1
}