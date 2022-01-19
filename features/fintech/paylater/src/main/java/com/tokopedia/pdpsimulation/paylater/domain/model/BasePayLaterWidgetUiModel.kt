package com.tokopedia.pdpsimulation.paylater.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.pdpsimulation.common.presentation.adapter.PayLaterAdapterFactory

interface BasePayLaterWidgetUiModel: Visitable<PayLaterAdapterFactory>

data class SimulationUiModel(
    val tenure: Int?,
    val text: String?,
    val smallText: String?,
    var simulationList: ArrayList<BasePayLaterWidgetUiModel>? = null
)