package com.tokopedia.recharge_component.listener

import com.tokopedia.recharge_component.model.denom.DenomData

interface RechargeDenomGridListener {
    fun onDenomGridClicked(denomGrid: DenomData, position: Int)
}