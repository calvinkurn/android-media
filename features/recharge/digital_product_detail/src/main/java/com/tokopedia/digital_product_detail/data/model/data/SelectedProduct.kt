package com.tokopedia.digital_product_detail.data.model.data

import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum

data class SelectedProduct (
    val denomData: DenomData = DenomData(),
    val denomWidgetEnum: DenomWidgetEnum = DenomWidgetEnum.GRID_TYPE,
    val position: Int = -1
)