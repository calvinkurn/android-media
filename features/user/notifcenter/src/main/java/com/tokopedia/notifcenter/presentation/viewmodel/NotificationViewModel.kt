package com.tokopedia.notifcenter.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.notifcenter.util.coroutines.DispatcherProvider
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
        dispatcher: DispatcherProvider
): BaseViewModel(dispatcher.io()) {

}