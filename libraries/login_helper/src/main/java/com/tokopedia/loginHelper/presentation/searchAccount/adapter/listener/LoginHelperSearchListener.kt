package com.tokopedia.loginHelper.presentation.searchAccount.adapter.listener

import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel

interface LoginHelperSearchListener {
    fun onEditAccount(user: UserDataUiModel)
    fun onDeleteAccount(user: UserDataUiModel)
}
