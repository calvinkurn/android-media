package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel

interface BusinessUnitItemViewListener {
    fun onReloadButtonClick()
    fun onSuccessGetData(data: HomeWidget)
    fun onErrorGetData(throwable: Throwable)
    fun onImpressed(element: BusinessUnitItemDataModel, position: Int)
}