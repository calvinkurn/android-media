package com.tokopedia.recharge_component.listener

import com.tokopedia.recharge_component.model.denom.DenomData

interface RechargeDenomFullListener {
    fun onDenomFullClicked(denomFull: DenomData, position: Int)
    fun onChevronDenomClicked(denomFull: DenomData, position: Int)
}