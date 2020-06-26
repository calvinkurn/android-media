package com.tokopedia.talk.feature.write.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import javax.inject.Inject

class TalkWriteViewModel @Inject constructor(private val dispatchers: CoroutineDispatchers): BaseViewModel(dispatchers.io) {

    fun getWriteFormData(productId: Int) {

    }

    fun submitWriteForm() {

    }
}