package com.tokopedia.recharge_component.listener

import com.tokopedia.recharge_component.model.denom.DenomWidgetModel

interface RechargeDenomGridListener {
    fun onDenomGridClicked(denomGrid: DenomWidgetModel, position: Int)
}