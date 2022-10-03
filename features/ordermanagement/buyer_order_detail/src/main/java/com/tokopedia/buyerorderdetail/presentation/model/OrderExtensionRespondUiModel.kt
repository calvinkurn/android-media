package com.tokopedia.buyerorderdetail.presentation.model

import java.io.Serializable

data class OrderExtensionRespondUiModel(
    val message: String = "",
    val messageCode: Int,
    val actionType: Int
) : Serializable
