package com.tokopedia.login_helper.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.login_helper.presentation.adapter.factory.LoginHelperAdapterFactoryImpl
import com.tokopedia.login_helper.presentation.adapter.viewholder.LoginHelperClickListener

class LoginHelperRecyclerAdapter(listener: LoginHelperClickListener) :
    BaseAdapter<LoginHelperAdapterFactoryImpl>(LoginHelperAdapterFactoryImpl(listener))
