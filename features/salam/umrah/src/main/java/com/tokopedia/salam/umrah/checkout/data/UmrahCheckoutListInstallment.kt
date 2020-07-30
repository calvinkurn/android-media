package com.tokopedia.salam.umrah.checkout.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by firman on 27/11/2019
 */

data class UmrahCheckoutListInstallment(
        @SerializedName("list")
        @Expose
        var list: List<UmrahCheckoutInstallment> = arrayListOf()
)

data class UmrahCheckoutInstallment(
        @SerializedName("installmentType")
        @Expose
        var installmentType: String = "",
        @SerializedName("installmentPrice")
        @Expose
        var installmentPrice: String = ""
)