package com.tokopedia.ordermanagement.buyercancellationorder.presentation.model

data class BuyerCancelRequestReasonValidationResult(
    val inputFieldMessage: String,
    val isError: Boolean,
    val isButtonEnable: Boolean
)