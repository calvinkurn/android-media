package com.tokopedia.play.view.uimodel


/**
 * Created by mzennis on 2020-03-05.
 */
data class CartUiModel(
        val isShow: Boolean = false,
        val count: Int = 0
)

data class CartFeedbackUiModel(
        val isSuccess: Boolean = false,
        val errorMessage: String = "",
        val isAtcOnly: Boolean = true
)