package com.tokopedia.login_helper.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.login_helper.domain.uiModel.LoginDataUiModel
import com.tokopedia.login_helper.presentation.adapter.factory.LoginHelperAdapterFactoryImpl
import com.tokopedia.login_helper.presentation.adapter.viewholder.LoginHelperClickListener

class LoginHelperRecyclerAdapter(listener: LoginHelperClickListener) :
    BaseAdapter<LoginHelperAdapterFactoryImpl>(LoginHelperAdapterFactoryImpl(listener)) {

    fun addData(loginDataList: LoginDataUiModel) {
        clearAllElements()
        addElement(loginDataList.count)
        addElement(loginDataList.users)
    }
}
