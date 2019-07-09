package com.tokopedia.checkout.view.feature.cartlist

import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartShops

interface InsuranceItemActionListener : ActionListener {
    fun deleteInsurance(insuranceCartShops: InsuranceCartShops)

    fun onInsuranceSelectStateChanges()

    // TODO: 19/6/19 add methods according to insurance items usecase ex: opening bottom sheet for application details

}