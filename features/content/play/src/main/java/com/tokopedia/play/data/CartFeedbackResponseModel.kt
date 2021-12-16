package com.tokopedia.play.data

/**
 * Created By : Jonathan Darwin on November 05, 2021
 */
data class CartFeedbackResponseModel(
    val isSuccess: Boolean,
    val errorMessage: String,
    val cartId: String
)