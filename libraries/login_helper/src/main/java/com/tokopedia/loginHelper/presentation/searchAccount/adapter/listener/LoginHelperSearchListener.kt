package com.tokopedia.loginHelper.presentation.searchAccount.adapter.listener

import com.tokopedia.loginHelper.domain.uiModel.UserDataUiModel

interface LoginHelperSearchListener {
    fun onEditAccount(user: UserDataUiModel)
    fun onDeleteAccount(user: UserDataUiModel)
}
