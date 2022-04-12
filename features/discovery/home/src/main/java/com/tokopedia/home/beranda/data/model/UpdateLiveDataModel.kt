package com.tokopedia.home.beranda.data.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel

data class UpdateLiveDataModel (
        val action: Int = -1,
        val visitable: Visitable<*>? = null,
        val position: Int = -1,
        val homeDynamicChannel: HomeDynamicChannelModel? = null,
        val needToEvaluateRecommendation: Boolean = false
)