package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName

data class GqlMerchantSaldoDetailsResponse(

        @SerializedName("sp_getmerchantstatus")
        var data: GqlDetailsResponse? = null

)
