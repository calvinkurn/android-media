package com.tokopedia.digital_product_detail.data.model

import com.tokopedia.recharge_component.model.denom.DenomWidgetModel

sealed class DigitalDenomGridUiModel {
    data class Success(val data: DenomWidgetModel): DigitalDenomGridUiModel()
    object Loading: DigitalDenomGridUiModel()
    object Error: DigitalDenomGridUiModel()
    object Unknown: DigitalDenomGridUiModel()
}