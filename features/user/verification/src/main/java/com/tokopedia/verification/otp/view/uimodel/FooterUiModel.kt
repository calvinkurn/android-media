package com.tokopedia.verification.otp.view.uimodel


data class FooterUiModel(
    val message: String,
    val spannableUiModel: List<SpannableUiModel>
) {
    data class SpannableUiModel(
        val clickableMessage: String,
        val action: () -> Unit
    )
}
