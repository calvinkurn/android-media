package com.tokopedia.loginHelper.presentation.home.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.loginHelper.domain.uiModel.LoginDataUiModel
import com.tokopedia.loginHelper.presentation.home.adapter.factory.LoginHelperAdapterFactoryImpl
import com.tokopedia.loginHelper.presentation.home.adapter.viewholder.LoginHelperClickListener

class LoginHelperRecyclerAdapter(listener: LoginHelperClickListener) :
    BaseAdapter<LoginHelperAdapterFactoryImpl>(LoginHelperAdapterFactoryImpl(listener)) {

    fun addData(loginDataList: LoginDataUiModel) {
        clearAllElements()
        addElement(loginDataList.count)
        addElement(loginDataList.users)
    }
}
