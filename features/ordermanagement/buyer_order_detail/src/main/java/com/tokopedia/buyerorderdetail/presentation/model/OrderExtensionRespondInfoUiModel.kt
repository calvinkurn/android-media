package com.tokopedia.buyerorderdetail.presentation.model

data class OrderExtensionRespondInfoUiModel(
    val confirmationTitle: String = "",
    val reasonExtension: String = "",
    val rejectText: String = "",
    val newDeadline: String = "",
    val messageCode: String = "",
    val message: String = ""
)