package com.tokopedia.product.estimasiongkir.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 16/02/21
 */
data class RatesInstallmentInformation(
        val tokoCabangData: TokoCabangData = TokoCabangData()
) {
    data class Response(
            @SerializedName("pdpGetDetailBottomSheet")
            @Expose
            val response: RatesInstallmentInformation = RatesInstallmentInformation()
    )
}

data class TokoCabangData(
        @SerializedName("html")
        @Expose
        val html: String = ""
)