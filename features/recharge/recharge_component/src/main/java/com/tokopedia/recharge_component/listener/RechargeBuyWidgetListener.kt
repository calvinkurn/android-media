package com.tokopedia.recharge_component.listener

import com.tokopedia.recharge_component.model.denom.DenomData

interface RechargeBuyWidgetListener {
    fun onClickedButtonLanjutkan(denom: DenomData)

    fun onClickedButtonMultiCheckout(denom: DenomData)
    fun onClickedChevron(denom: DenomData)
}
