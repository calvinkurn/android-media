package com.tokopedia.tokomart.home.presentation.uimodel

import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeTypeFactory

data class HomeSliderBannerUiModel(
        val channelModel: ChannelModel? = null,
        val isCache: Boolean = false
):  HomeLayoutUiModel, ImpressHolder() {
    override fun type(typeFactory: TokoMartHomeTypeFactory): Int {
        return typeFactory.type(this)
    }

}