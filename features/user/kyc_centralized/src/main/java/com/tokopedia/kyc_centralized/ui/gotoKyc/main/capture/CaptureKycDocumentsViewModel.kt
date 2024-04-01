package com.tokopedia.kyc_centralized.ui.gotoKyc.main.capture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class CaptureKycDocumentsViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private var _failedCounter = 0
    private val _retryExhausted = MutableLiveData<Unit>()
    val retryExhausted : LiveData<Unit> get() = _retryExhausted

    fun onFailedUpload() {
        _failedCounter++
        if (_failedCounter > 3) {
            _retryExhausted.value = Unit
        }
    }

}
