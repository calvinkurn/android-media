package com.tokopedia.shopdiscount.select.domain.entity

data class ProblematicReservableProduct(
    val id: String,
    val errorMessage: String,
    val errorCode: Int
)