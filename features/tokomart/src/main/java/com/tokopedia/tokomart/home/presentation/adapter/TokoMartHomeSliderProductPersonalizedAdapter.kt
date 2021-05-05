package com.tokopedia.tokomart.home.presentation.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.tokomart.home.presentation.adapter.delegate.TokoMartHomeSliderProductPersonalizedDelegate
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSliderProductPersonalizedUiModel

class TokoMartHomeSliderProductPersonalizedAdapter: BaseDiffUtilAdapter<HomeSliderProductPersonalizedUiModel>() {

    init {
        delegatesManager.addDelegate(TokoMartHomeSliderProductPersonalizedDelegate())
    }

    override fun areItemsTheSame(oldItem: HomeSliderProductPersonalizedUiModel, newItem: HomeSliderProductPersonalizedUiModel): Boolean =
            oldItem.title == newItem.title && oldItem.price == newItem.price

    override fun areContentsTheSame(oldItem: HomeSliderProductPersonalizedUiModel, newItem: HomeSliderProductPersonalizedUiModel): Boolean =
            oldItem == newItem
}

