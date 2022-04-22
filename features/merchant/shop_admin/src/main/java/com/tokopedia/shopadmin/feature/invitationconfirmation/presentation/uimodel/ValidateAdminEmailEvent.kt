package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel

sealed class ValidateAdminEmailEvent {
    data class Success(val validateAdminEmailUiModel: ValidateAdminEmailUiModel) : ValidateAdminEmailEvent()
    data class Error(val error: Throwable) : ValidateAdminEmailEvent()
}