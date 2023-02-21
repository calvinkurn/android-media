package com.tokopedia.login_helper.presentation.viewmodel.state

import com.tokopedia.login_helper.domain.LoginHelperEnvType

data class LoginHelperUiState(
    val envType: LoginHelperEnvType = LoginHelperEnvType.STAGING
)
