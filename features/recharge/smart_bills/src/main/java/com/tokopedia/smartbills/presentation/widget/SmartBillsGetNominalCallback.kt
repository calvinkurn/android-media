package com.tokopedia.smartbills.presentation.widget

import com.tokopedia.smartbills.data.RechargeProduct

interface SmartBillsGetNominalCallback {
    fun onProductClicked(rechargeProduct: RechargeProduct)
}