package com.tokopedia.pdpsimulation.activateCheckout.listner

import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel

interface TenureSelectListner {

    fun selectedTenure(tenureSelectedModel: TenureSelectedModel)

}