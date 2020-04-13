package com.tokopedia.talk.feature.report.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import javax.inject.Inject

class TalkReportViewModel @Inject constructor(
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {
}