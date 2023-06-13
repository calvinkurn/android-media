package com.tokopedia.loginHelper.presentation.adapter.factory

import com.tokopedia.loginHelper.domain.uiModel.HeaderUiModel
import com.tokopedia.loginHelper.domain.uiModel.UserDataUiModel

interface LoginHelperAdapterFactory {

    fun type(model: UserDataUiModel): Int

    fun type(model: HeaderUiModel): Int
}
