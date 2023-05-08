package com.tokopedia.digital_product_detail.data.model.data

import com.tokopedia.recharge_component.model.denom.DenomWidgetModel

data class InputMultiTabDenomModel(
    val denomFull: DenomWidgetModel = DenomWidgetModel(),
    val denomMCCMFull: DenomWidgetModel = DenomWidgetModel(),
    val otherComponents: List<TelcoOtherComponent> = emptyList(),
    val filterTagComponents: List<TelcoFilterTagComponent> = emptyList(),
    val isFilterRefreshed: Boolean = true
)
