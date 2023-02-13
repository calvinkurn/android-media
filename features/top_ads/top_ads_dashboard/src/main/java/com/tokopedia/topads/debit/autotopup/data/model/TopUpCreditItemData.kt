package com.tokopedia.topads.debit.autotopup.data.model

data class TopUpCreditItemData(
    val productPrice: String = "",
    val bonus: String = "",
    var clicked: Boolean = false
)
