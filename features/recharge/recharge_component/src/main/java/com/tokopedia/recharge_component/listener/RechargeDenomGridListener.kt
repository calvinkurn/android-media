package com.tokopedia.recharge_component.listener

import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum

interface RechargeDenomGridListener {
    fun onDenomGridClicked(denomGrid: DenomData, layoutType: DenomWidgetEnum, position: Int,
                           productListTitle: String,
                           isShowBuyWidget: Boolean)
    fun onDenomGridImpression(denomGrid: DenomData, layoutType: DenomWidgetEnum, position: Int)
}