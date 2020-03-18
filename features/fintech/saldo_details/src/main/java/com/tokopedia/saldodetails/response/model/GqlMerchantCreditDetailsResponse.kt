package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName

data class GqlMerchantCreditDetailsResponse(

        @SerializedName("mcl_getmclstatus")
        var data: GqlMerchantCreditResponse? = null
)
