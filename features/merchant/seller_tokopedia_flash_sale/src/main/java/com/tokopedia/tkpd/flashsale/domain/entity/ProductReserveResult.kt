package com.tokopedia.tkpd.flashsale.domain.entity

data class ProductReserveResult(
    val isSuccess : Boolean,
    val errorMessage: String,
    val reservationId: String
)
