package com.tokopedia.product.manage.item.main.base.view.listener

import com.tokopedia.product.manage.item.common.util.AddEditPageType
import com.tokopedia.user.session.UserSessionInterface

interface ListenerOnAddEditFragmentCreated {
    fun setUserSession(userSessionInterface: UserSessionInterface)
}