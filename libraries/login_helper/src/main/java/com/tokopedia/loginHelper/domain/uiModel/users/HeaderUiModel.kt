package com.tokopedia.loginHelper.domain.uiModel.users

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.loginHelper.presentation.home.adapter.factory.LoginHelperAdapterFactory

data class HeaderUiModel(
    val userCount: Int = 0,
    val title: String = ""
) : Visitable<LoginHelperAdapterFactory> {

    override fun type(typeFactory: LoginHelperAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
