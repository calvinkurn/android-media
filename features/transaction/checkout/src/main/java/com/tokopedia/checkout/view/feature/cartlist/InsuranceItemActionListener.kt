package com.tokopedia.checkout.view.feature.cartlist

import com.tokopedia.transactiondata.insurance.entity.request.UpdateInsuranceProductApplicationDetails
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartDigitalProduct
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartShops
import java.util.ArrayList

interface InsuranceItemActionListener : ActionListener {
    fun deleteMacroInsurance(insuranceCartDigitalProductList: ArrayList<InsuranceCartDigitalProduct>, showconfirmationDialog: Boolean)
    fun deleteMicroInsurance(insuranceCartDigitalProductList: ArrayList<InsuranceCartDigitalProduct>, showconfirmationDialog: Boolean)
    fun updateInsuranceProductData(insuranceCartShops: InsuranceCartShops, updateInsuranceProductApplicationDetailsArrayList: ArrayList<UpdateInsuranceProductApplicationDetails>)
    fun onInsuranceSelectStateChanges()
}