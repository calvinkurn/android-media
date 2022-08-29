package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel

data class ValidateAdminEmailUiModel(
    val isSuccess: Boolean,
    val message: String,
    val existsUser: Boolean
)