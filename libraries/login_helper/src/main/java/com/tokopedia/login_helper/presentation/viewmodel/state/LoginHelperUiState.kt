package com.tokopedia.login_helper.presentation.viewmodel.state

import com.tokopedia.login_helper.domain.LoginHelperEnvType
import com.tokopedia.login_helper.domain.uiModel.LoginDataUiModel

data class LoginHelperUiState(
    val envType: LoginHelperEnvType = LoginHelperEnvType.STAGING,
    val loginData: LoginDataUiModel? = null,
)
