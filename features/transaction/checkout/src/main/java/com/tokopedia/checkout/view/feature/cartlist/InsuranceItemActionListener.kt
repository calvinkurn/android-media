package com.tokopedia.checkout.view.feature.cartlist

import com.tokopedia.transactiondata.insurance.entity.request.UpdateInsuranceProductApplicationDetails
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartShops
import java.util.ArrayList

interface InsuranceItemActionListener : ActionListener {
    fun deleteInsurance(insuranceCartShops: InsuranceCartShops, showconfirmationDialog: Boolean)
    fun updateInsuranceProductData(insuranceCartShops: InsuranceCartShops, updateInsuranceProductApplicationDetailsArrayList: ArrayList<UpdateInsuranceProductApplicationDetails>)
    fun onInsuranceSelectStateChanges()

    // TODO: 19/6/19 add methods according to insurance items usecase ex: opening bottom sheet for application details

}