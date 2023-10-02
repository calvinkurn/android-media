package com.tokopedia.sellerorder.common.presenter.model

/**
 * Created by irpan on 06/09/23.
 */
data class ConfirmShippingNotes(
    val noteText: String = "",
    val url: String = "",
    val urlText: String = "",
    val spanStart: Int = 0,
    val spanEnd: Int = 0
)
