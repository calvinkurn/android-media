package com.tokopedia.pdpsimulation.activateCheckout.listner

import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel

interface ActivationListner {
    fun checkIsDisablePartner(): Boolean
    fun selectedTenure(
        tenureSelectedModel: TenureSelectedModel,
        newPositionToSelect: Int,
        promoName: String,
    )
}
