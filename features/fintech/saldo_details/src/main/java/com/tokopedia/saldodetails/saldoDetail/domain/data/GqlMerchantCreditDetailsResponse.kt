package com.tokopedia.saldodetails.saldoDetail.domain.data

import com.google.gson.annotations.SerializedName

data class GqlMerchantCreditDetailsResponse(

        @SerializedName("mcl_getmclstatus")
        var data: GqlMerchantCreditResponse? = null
)
