package com.tokopedia.otp.notif.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.otp.common.DispatcherProvider
import javax.inject.Inject

class NotifViewModel @Inject constructor(
        dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider.ui()) {

}