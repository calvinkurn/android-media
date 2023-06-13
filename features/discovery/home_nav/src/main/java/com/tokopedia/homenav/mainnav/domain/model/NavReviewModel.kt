package com.tokopedia.homenav.mainnav.domain.model

data class NavReviewModel(
    val productId: String = "",
    val productName: String = "",
    val imageUrl: String = "",
    val reputationId: String = "",
    var fullWidth: Boolean = false
)
