package com.tokopedia.common.topupbills.favoritepdp.domain.model

data class PrefillModel(
    val clientName: String = "",
    val clientNumber: String = "",
    val token: String = "",
    val operatorId: String = "",
    val productId: String = "",
)