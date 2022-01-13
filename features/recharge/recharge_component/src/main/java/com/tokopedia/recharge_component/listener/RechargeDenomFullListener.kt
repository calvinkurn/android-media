package com.tokopedia.recharge_component.listener

import com.tokopedia.recharge_component.model.denom.DenomWidgetModel

interface RechargeDenomFullListener {
    fun onDenomFullClicked(denomGrid: DenomWidgetModel, position: Int)
    fun onChevronDenomClicked(denomGrid: DenomWidgetModel, position: Int)
}