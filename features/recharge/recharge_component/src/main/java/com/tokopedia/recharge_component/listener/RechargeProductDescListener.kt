package com.tokopedia.recharge_component.listener

import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum

interface RechargeProductDescListener {
    fun onCloseProductBottomSheet(denomData: DenomData, layoutType: DenomWidgetEnum)
    fun onImpressProductBottomSheet(denomData: DenomData, layoutType: DenomWidgetEnum, productListTitle: String, position: Int)
}
