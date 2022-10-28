package com.tokopedia.recharge_component.model.denom

data class DenomMCCMModel(
    val denomWidgetModel: DenomWidgetModel = DenomWidgetModel(),
    val mccmFlashSaleModel: DenomWidgetModel = DenomWidgetModel()
)