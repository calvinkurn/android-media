package com.tokopedia.login_helper.domain.uiModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.login_helper.presentation.adapter.factory.LoginHelperAdapterFactory

data class UserDataUiModel(
    val email: String?,
    val password: String?,
    val tribe: String?
): Visitable<LoginHelperAdapterFactory> {

    override fun type(typeFactory: LoginHelperAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
