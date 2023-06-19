package com.tokopedia.loginHelper.presentation.home.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.loginHelper.domain.uiModel.UnifiedLoginHelperData
import com.tokopedia.loginHelper.presentation.home.adapter.factory.LoginHelperAdapterFactoryImpl
import com.tokopedia.loginHelper.presentation.home.adapter.viewholder.LoginHelperClickListener

class LoginHelperRecyclerAdapter(listener: LoginHelperClickListener) :
    BaseAdapter<LoginHelperAdapterFactoryImpl>(LoginHelperAdapterFactoryImpl(listener)) {

    fun addData(loginDataList: UnifiedLoginHelperData?) {
        loginDataList?.persistentCacheUserData?.let {
            addElement(it.count)
            addElement(it.users)
        }
        loginDataList?.remoteUserData?.let {
            if (it.users != null) {
                addElement(it.count)
                addElement(it.users)
            }
        }
    }
}
