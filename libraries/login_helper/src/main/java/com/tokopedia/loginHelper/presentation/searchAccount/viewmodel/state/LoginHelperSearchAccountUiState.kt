package com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.state

import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.users.LoginDataUiModel

data class LoginHelperSearchAccountUiState(
    val envType: LoginHelperEnvType = LoginHelperEnvType.STAGING,
    val loginDataList: com.tokopedia.usecase.coroutines.Result<LoginDataUiModel>? = null,
    val searchText: String = "",
    val filteredUserList: com.tokopedia.usecase.coroutines.Result<LoginDataUiModel>? = null,
    val isLoading: Boolean = false
)
