package com.tokopedia.loginHelper.presentation.home.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.loginHelper.domain.uiModel.users.HeaderUiModel
import com.tokopedia.loginHelper.domain.uiModel.users.UserDataUiModel
import com.tokopedia.loginHelper.presentation.home.adapter.viewholder.LoginDataViewHolder
import com.tokopedia.loginHelper.presentation.home.adapter.viewholder.LoginHeaderViewHolder
import com.tokopedia.loginHelper.presentation.home.adapter.viewholder.LoginHelperClickListener

class LoginHelperAdapterFactoryImpl(private val listener: LoginHelperClickListener) :
    BaseAdapterTypeFactory(),
    LoginHelperAdapterFactory {
    override fun type(model: UserDataUiModel): Int {
        return LoginDataViewHolder.RES_LAYOUT
    }

    override fun type(model: HeaderUiModel): Int {
        return LoginHeaderViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            LoginDataViewHolder.RES_LAYOUT -> LoginDataViewHolder(parent, listener)
            LoginHeaderViewHolder.RES_LAYOUT -> LoginHeaderViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
