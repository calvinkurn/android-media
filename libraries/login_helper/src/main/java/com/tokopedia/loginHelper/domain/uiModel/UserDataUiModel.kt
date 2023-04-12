package com.tokopedia.loginHelper.domain.uiModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.loginHelper.presentation.home.adapter.factory.LoginHelperAdapterFactory

data class UserDataUiModel(
    val email: String?,
    val password: String?,
    val tribe: String?
) : Visitable<LoginHelperAdapterFactory> {

    override fun type(typeFactory: LoginHelperAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
