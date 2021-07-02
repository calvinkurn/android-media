package com.tokopedia.buyerorder.detail.view.model

data class BuyerCancelRequestReasonValidationResult(
        val inputFieldMessage: String,
        val isError: Boolean,
        val isButtonEnable: Boolean
)