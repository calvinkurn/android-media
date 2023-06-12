package com.tokopedia.loginHelper.presentation.home.adapter.factory

import com.tokopedia.loginHelper.domain.uiModel.users.HeaderUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel

interface LoginHelperAdapterFactory {

    fun type(model: UserDataUiModel): Int

    fun type(model: HeaderUiModel): Int
}
