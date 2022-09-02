package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel

data class AdminConfirmationRegUiModel(
    val isSuccess: Boolean,
    val message: String = "",
    val acceptBecomeAdmin: Boolean
)