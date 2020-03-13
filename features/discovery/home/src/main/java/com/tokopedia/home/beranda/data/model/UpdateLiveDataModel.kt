package com.tokopedia.home.beranda.data.model

import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel

data class UpdateLiveDataModel (
        val action: Int,
        val visitable: HomeVisitable? = null,
        val position: Int = -1,
        val homeData: HomeDataModel? = null
)