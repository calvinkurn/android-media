package com.tokopedia.login_helper.presentation.adapter.factory

import com.tokopedia.login_helper.domain.uiModel.HeaderUiModel
import com.tokopedia.login_helper.domain.uiModel.LoginDataUiModel
import com.tokopedia.login_helper.domain.uiModel.UserDataUiModel

interface LoginHelperAdapterFactory {

    fun type(model: UserDataUiModel): Int

    fun type(model: HeaderUiModel): Int
}
