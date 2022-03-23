package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model

data class ValidateEmailUiModel(
    val isSuccess: Boolean = false,
    val message: String = "",
    val existsUser: Boolean = false
)