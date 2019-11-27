package com.tokopedia.salam.umrah.checkout.data

data class UmrahCheckoutListInstallment(
        var list: List<UmrahCheckoutInstallment> = arrayListOf()
)

data class UmrahCheckoutInstallment(
        var installmentType: String = "",
        var installmentPrice: String = ""
)