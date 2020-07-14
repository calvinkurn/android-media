package com.tokopedia.withdraw.saldowithdrawal.domain.model

import com.google.gson.annotations.SerializedName

data class GqlRekPreTncResponse(
        @SerializedName("GetTNC")
        val rekPreTncResponse: RekPreTncResponse
)

data class RekPreTncResponse(
        @SerializedName("data")
        val rekPreTncResponseData: TermTemplate
)

data class TermTemplate(
        @SerializedName("template")
        val template: String
)
