package com.tokopedia.loginHelper.presentation.home.viewmodel.state

import com.tokopedia.loginHelper.domain.LoginHelperDataSourceType
import com.tokopedia.loginHelper.domain.LoginHelperEnvType
import com.tokopedia.loginHelper.domain.uiModel.UnifiedLoginHelperData
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.profile.ProfilePojo

data class LoginHelperUiState(
    val envType: LoginHelperEnvType = LoginHelperEnvType.STAGING,
    // From Remote
    val loginDataList: com.tokopedia.usecase.coroutines.Result<UnifiedLoginHelperData>? = null,
    val filteredUserList: com.tokopedia.usecase.coroutines.Result<UnifiedLoginHelperData>? = null,
    val loginToken: com.tokopedia.usecase.coroutines.Result<LoginToken>? = null,
    val profilePojo: com.tokopedia.usecase.coroutines.Result<ProfilePojo>? = null,
    val searchText: String = "",
    val dataSourceType: LoginHelperDataSourceType = LoginHelperDataSourceType.REMOTE,
    // From File
    val localLoginDataList: com.tokopedia.usecase.coroutines.Result<UnifiedLoginHelperData>? = null,
    val localFilteredLoginDataList: com.tokopedia.usecase.coroutines.Result<UnifiedLoginHelperData>? = null,
    val isLoading: Boolean = true
)
