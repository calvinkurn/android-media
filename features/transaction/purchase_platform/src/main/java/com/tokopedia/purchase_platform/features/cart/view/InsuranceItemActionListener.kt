package com.tokopedia.purchase_platform.features.cart.view

import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProductApplicationDetails
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShops
import java.util.ArrayList

interface InsuranceItemActionListener {
    fun sendEventDeleteInsurance(insuranceTitle: String)
    fun sendEventChangeInsuranceState(isChecked: Boolean, insuranceTitle: String)
    fun deleteMacroInsurance(insuranceCartDigitalProductList: ArrayList<InsuranceCartDigitalProduct>, showConfirmationDialog: Boolean)
    fun updateInsuranceProductData(insuranceCartShops: InsuranceCartShops, updateInsuranceProductApplicationDetailsArrayList: ArrayList<UpdateInsuranceProductApplicationDetails>)
    fun onInsuranceSelectStateChanges()
    fun sendEventInsuranceImpression(title: String)
    fun sendEventInsuranceImpressionForShipment(title: String)
}