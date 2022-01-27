package com.tokopedia.recharge_component.listener

import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum

interface RechargeDenomFullListener {
    fun onDenomFullClicked(denomFull: DenomData, layoutType: DenomWidgetEnum, position: Int,
                           productListTitle: String,
                           isShowBuyWidget: Boolean)
    fun onDenomFullImpression(denomFull: DenomData, layoutType: DenomWidgetEnum, position: Int)
    fun onChevronDenomClicked(denomFull: DenomData, position: Int)
}