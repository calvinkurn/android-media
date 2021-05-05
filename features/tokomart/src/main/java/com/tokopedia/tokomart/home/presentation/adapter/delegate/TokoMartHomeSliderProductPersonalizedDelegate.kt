package com.tokopedia.tokomart.home.presentation.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSliderProductPersonalizedUiModel
import com.tokopedia.tokomart.home.presentation.viewholder.HomeProductPersonalizedViewHolder

class TokoMartHomeSliderProductPersonalizedDelegate: TypedAdapterDelegate<HomeSliderProductPersonalizedUiModel, HomeSliderProductPersonalizedUiModel, HomeProductPersonalizedViewHolder>(HomeProductPersonalizedViewHolder.LAYOUT) {

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): HomeProductPersonalizedViewHolder {
        return  HomeProductPersonalizedViewHolder(basicView)
    }

    override fun onBindViewHolder(item: HomeSliderProductPersonalizedUiModel, holder: HomeProductPersonalizedViewHolder) {
        holder.bind(item)
    }
}