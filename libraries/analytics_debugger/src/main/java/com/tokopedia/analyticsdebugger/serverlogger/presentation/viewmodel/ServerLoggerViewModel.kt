package com.tokopedia.analyticsdebugger.serverlogger.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class ServerLoggerViewModel @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers
): BaseViewModel(coroutineDispatchers.main) {

}