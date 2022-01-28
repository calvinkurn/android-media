package com.tokopedia.digital_product_detail.data.model.data

import com.tokopedia.recharge_component.model.denom.DenomWidgetModel

data class InputMultiTabDenomModel(
    val denomFull: DenomWidgetModel = DenomWidgetModel(),
    val denomMCCMFull: DenomWidgetModel = DenomWidgetModel(),
    val filterTagComponents: List<TelcoFilterTagComponent> = mutableListOf()
)