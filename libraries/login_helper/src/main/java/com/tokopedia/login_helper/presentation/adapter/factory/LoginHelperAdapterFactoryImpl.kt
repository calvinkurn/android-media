package com.tokopedia.login_helper.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.login_helper.domain.uiModel.HeaderUiModel
import com.tokopedia.login_helper.domain.uiModel.LoginDataUiModel
import com.tokopedia.login_helper.domain.uiModel.UserDataUiModel
import com.tokopedia.login_helper.presentation.adapter.viewholder.LoginDataViewHolder
import com.tokopedia.login_helper.presentation.adapter.viewholder.LoginHeaderViewHolder

class LoginHelperAdapterFactoryImpl : BaseAdapterTypeFactory(), LoginHelperAdapterFactory {
    override fun type(model: UserDataUiModel): Int {
        return LoginDataViewHolder.RES_LAYOUT
    }

    override fun type(model: HeaderUiModel): Int {
        return LoginHeaderViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            LoginDataViewHolder.RES_LAYOUT -> LoginDataViewHolder(parent)
            LoginHeaderViewHolder.RES_LAYOUT -> LoginHeaderViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
