package com.tokopedia.smartbills.presentation.widget

import com.tokopedia.smartbills.data.RechargeCatalogProductInputMultiTabData
import com.tokopedia.smartbills.data.RechargeProduct

interface SmartBillsGetNominalCallback {
    fun onProductClicked(rechargeProduct: RechargeProduct)
    fun onNominalLoaded(isRequestNominal: Boolean, catalogProduct: RechargeCatalogProductInputMultiTabData, products: List<RechargeProduct>)
    fun onCloseNominal()
}