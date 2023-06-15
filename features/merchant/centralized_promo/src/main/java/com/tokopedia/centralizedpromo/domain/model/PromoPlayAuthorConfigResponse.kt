package com.tokopedia.centralizedpromo.domain.model

import com.google.gson.annotations.SerializedName

data class PromoPlayAuthorConfigResponse(
    @SerializedName("broadcasterGetAuthorConfig")
    val authorConfig: PromoPlayAuthorConfig = PromoPlayAuthorConfig()
)

data class PromoPlayAuthorConfig(
    @SerializedName("hasContent")
    val hasContent: Boolean = false
)
