package com.tokopedia.inbox.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.inbox.common.InboxCoroutineContextProvider
import javax.inject.Inject

class InboxViewModel @Inject constructor(
        private val dispatchers: InboxCoroutineContextProvider
) : BaseViewModel(dispatchers.IO) {


}