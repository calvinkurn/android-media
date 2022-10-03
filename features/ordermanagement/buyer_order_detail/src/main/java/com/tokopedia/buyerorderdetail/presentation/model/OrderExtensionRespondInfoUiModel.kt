package com.tokopedia.buyerorderdetail.presentation.model

import java.io.Serializable

data class OrderExtensionRespondInfoUiModel(
    val orderId: String = "",
    val confirmationTitle: String = "",
    val reasonExtension: String = "",
    val rejectText: String = "",
    val newDeadline: String = "",
    val messageCode: Int,
    val message: String = ""
) : Serializable
