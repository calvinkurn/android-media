package com.tokopedia.pdpsimulation.paylater.domain.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.pdpsimulation.paylater.presentation.adapter.PayLaterAdapterFactory

interface BasePayLaterWidgetUiModel : Visitable<PayLaterAdapterFactory>

data class SimulationUiModel(
    val tenure: Int?,
    val text: String?,
    val smallText: String?,
    var isSelected: Boolean = false,
    var simulationList: ArrayList<BasePayLaterWidgetUiModel>? = null,
    var label: LabelUiModel? = null,
) {
    data class LabelUiModel(
        val text: String = "",
        val textColor: String = "",
        val bgColor: String = "",
    ) {

        companion object {
            fun create(labelData: Label?) = labelData?.let {
                LabelUiModel(it.text, it.textColor, it.bgColor)
            }
        }
    }
}
