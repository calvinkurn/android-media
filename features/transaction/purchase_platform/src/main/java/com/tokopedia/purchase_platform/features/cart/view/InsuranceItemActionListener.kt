package com.tokopedia.purchase_platform.features.cart.view

import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProductApplicationDetails
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartShops
import java.util.ArrayList

interface InsuranceItemActionListener {
    fun deleteMacroInsurance(insuranceCartDigitalProductList: ArrayList<InsuranceCartDigitalProduct>, showconfirmationDialog: Boolean)
    fun updateInsuranceProductData(insuranceCartShops: InsuranceCartShops, updateInsuranceProductApplicationDetailsArrayList: ArrayList<UpdateInsuranceProductApplicationDetails>)
    fun onInsuranceSelectStateChanges()
}