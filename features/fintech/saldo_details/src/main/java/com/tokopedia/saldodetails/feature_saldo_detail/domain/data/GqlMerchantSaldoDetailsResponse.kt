package com.tokopedia.saldodetails.feature_saldo_detail.domain.data

import com.google.gson.annotations.SerializedName

data class GqlMerchantSaldoDetailsResponse(

        @SerializedName("sp_getmerchantstatus")
        var data: GqlDetailsResponse? = null

)
