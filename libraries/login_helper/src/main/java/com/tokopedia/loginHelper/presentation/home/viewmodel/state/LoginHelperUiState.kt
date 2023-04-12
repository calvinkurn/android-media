package com.tokopedia.loginHelper.presentation.home.viewmodel.state

import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.LoginDataUiModel
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.profile.ProfilePojo

data class LoginHelperUiState(
    val envType: LoginHelperEnvType = LoginHelperEnvType.STAGING,
    val loginDataList: com.tokopedia.usecase.coroutines.Result<LoginDataUiModel>? = null,
    val loginToken: com.tokopedia.usecase.coroutines.Result<LoginToken>? = null,
    val profilePojo: com.tokopedia.usecase.coroutines.Result<ProfilePojo>? = null,
    val searchText: String = "",
    val filteredUserList: com.tokopedia.usecase.coroutines.Result<LoginDataUiModel>? = null
)
