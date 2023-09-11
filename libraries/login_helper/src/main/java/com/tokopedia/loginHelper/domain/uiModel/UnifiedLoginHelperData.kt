package com.tokopedia.loginHelper.domain.uiModel

import com.tokopedia.loginHelper.domain.uiModel.users.LoginDataUiModel

data class UnifiedLoginHelperData(
    val persistentCacheUserData: LoginDataUiModel? = null,
    val remoteUserData: LoginDataUiModel? = null
)
