package com.tokopedia.login_helper.presentation.adapter.factory

import com.tokopedia.login_helper.domain.uiModel.HeaderUiModel
import com.tokopedia.login_helper.domain.uiModel.LoginDataUiModel

interface LoginHelperAdapterFactory {

    fun type(model: LoginDataUiModel): Int

    fun type(model: HeaderUiModel): Int
}
