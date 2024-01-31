package com.tokopedia.topads.common.domain.model

import com.google.gson.annotations.SerializedName

data class GetVariantByIdResponse(
    @SerializedName("GetVariantById")
    val getVariantById: GetVariantById
) {
    data class GetVariantById(
        @SerializedName("userIdVariants")
        val userIdVariants: List<ExperimentVariant>,
        @SerializedName("shopIdVariants")
        val shopIdVariants: List<ExperimentVariant>,
        @SerializedName("sessionIdVariants")
        val sessionIdVariants: List<ExperimentVariant>
    ) {
        data class ExperimentVariant(
            @SerializedName("experiment")
            val experiment: String,
            @SerializedName("variant")
            val variant: String
        )
    }
}

data class GetVariantByIdInput(
    @SerializedName("shopId")
    val shopId: Long,
    @SerializedName("clientId")
    val clientId: Long
) {}
