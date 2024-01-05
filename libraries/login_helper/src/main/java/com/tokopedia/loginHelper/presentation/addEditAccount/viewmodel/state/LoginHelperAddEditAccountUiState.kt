package com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state

import com.tokopedia.loginHelper.domain.LoginHelperEnvType

data class LoginHelperAddEditAccountUiState(
    val envType: LoginHelperEnvType = LoginHelperEnvType.STAGING,
    val isLoading: Boolean = false
)
