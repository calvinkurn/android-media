package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model

data class AdminConfirmationRegUiModel(
    val isSuccess: Boolean,
    val message: String = "",
    val acceptBecomeAdmin: Boolean
)