package com.tokopedia.talk.feature.reply.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import javax.inject.Inject

class TalkReplyViewModel @Inject constructor(
        dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {
}