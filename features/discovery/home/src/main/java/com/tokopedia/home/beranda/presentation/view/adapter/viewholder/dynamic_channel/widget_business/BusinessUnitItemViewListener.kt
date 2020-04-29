package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel

interface BusinessUnitItemViewListener {
    fun onImpressed(element: BusinessUnitItemDataModel, position: Int)
    fun onClicked(position: Int)
}