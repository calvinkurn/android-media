package com.tokopedia.loginHelper.domain.uiModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.loginHelper.presentation.adapter.factory.LoginHelperAdapterFactory

data class HeaderUiModel(
    val userCount: Int
): Visitable<LoginHelperAdapterFactory> {

    override fun type(typeFactory: LoginHelperAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
