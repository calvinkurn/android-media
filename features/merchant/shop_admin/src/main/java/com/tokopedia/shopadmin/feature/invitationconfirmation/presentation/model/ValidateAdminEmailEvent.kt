package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model

sealed class ValidateAdminEmailEvent {
    data class Success(val validateEmailUiModel: ValidateEmailUiModel) : ValidateAdminEmailEvent()
    data class Error(val error: Throwable) : ValidateAdminEmailEvent()
}