package com.tokopedia.officialstore.official.data.model

import com.google.gson.annotations.SerializedName

data class OfficialStoreBenefits(
    @SerializedName("benefits")
    val benefits: MutableList<Benefit> = mutableListOf()
) {
    data class Response(
            @SerializedName("OfficialStoreBenefits")
            val officialStoreBenefits : OfficialStoreBenefits = OfficialStoreBenefits()
    )
}