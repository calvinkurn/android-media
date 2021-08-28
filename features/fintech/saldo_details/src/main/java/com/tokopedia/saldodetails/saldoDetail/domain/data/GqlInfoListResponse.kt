package com.tokopedia.saldodetails.saldoDetail.domain.data

import com.google.gson.annotations.SerializedName

class GqlInfoListResponse(

        @SerializedName("label")
        var label: String? = null,

        @SerializedName("value")
        var value: String? = null

)
