package com.tokopedia.pdpsimulation.activateCheckout.domain.model

import android.os.Parcelable
import com.tokopedia.pdpsimulation.paylater.domain.model.InstallmentDetails
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InstallmentBottomSheetDetail(
    var gatwayToChipMap :Map<Int, CheckoutData>,
    var gatewayIdSelected:Int,
    var selectedProductPrice: String = "",
    var selectedTenure :Int  ,
    var  installmentDetail :InstallmentDetails

):Parcelable
