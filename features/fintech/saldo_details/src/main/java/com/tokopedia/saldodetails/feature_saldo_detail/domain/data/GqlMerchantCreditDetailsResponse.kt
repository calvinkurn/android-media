package com.tokopedia.saldodetails.feature_saldo_detail.domain.data

import com.google.gson.annotations.SerializedName

data class GqlMerchantCreditDetailsResponse(

        @SerializedName("mcl_getmclstatus")
        var data: GqlMerchantCreditResponse? = null
)
