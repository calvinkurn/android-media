package com.tokopedia.salam.umrah.checkout.data

/**
 * @author by firman on 27/11/2019
 */

data class UmrahCheckoutListInstallment(
        var list: List<UmrahCheckoutInstallment> = arrayListOf()
)

data class UmrahCheckoutInstallment(
        var installmentType: String = "",
        var installmentPrice: String = ""
)