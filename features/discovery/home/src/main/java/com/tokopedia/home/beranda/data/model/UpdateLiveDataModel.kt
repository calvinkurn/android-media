package com.tokopedia.home.beranda.data.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel

data class UpdateLiveDataModel (
        val action: Int = -1,
        val visitable: Visitable<*>? = null,
        val position: Int = -1,
        val homeData: HomeDataModel? = null,
        val needToEvaluateRecommendation: Boolean = false
)