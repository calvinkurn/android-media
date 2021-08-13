package com.tokopedia.saldodetails.feature_saldo_detail.domain.data

import com.google.gson.annotations.SerializedName

class GqlInfoListResponse(

        @SerializedName("label")
        var label: String? = null,

        @SerializedName("value")
        var value: String? = null

)
