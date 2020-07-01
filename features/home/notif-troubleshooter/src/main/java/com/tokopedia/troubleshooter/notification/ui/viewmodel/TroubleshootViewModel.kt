package com.tokopedia.troubleshooter.notification.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.troubleshooter.notification.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.entity.NotifierSendTroubleshooter
import com.tokopedia.troubleshooter.notification.util.DispatcherProvider
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface TroubleshootContract {
    fun troubleshoot()
    fun updateToken()
}

class TroubleshootViewModel @Inject constructor(
        private val useCase: TroubleshootStatusUseCase,
        private val dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher.io()), TroubleshootContract {

    private val _troubleshoot = MutableLiveData<NotifierSendTroubleshooter>()
    val troubleshoot: LiveData<NotifierSendTroubleshooter> get() = _troubleshoot

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    override fun troubleshoot() {
        launchCatchError(block = {
            val result = useCase.execute(RequestParams.EMPTY)
            withContext(dispatcher.main()) {
                _troubleshoot.value = result.notifierSendTroubleshooter
            }
        }, onError = {
            _error.value = it
        })
    }

    override fun updateToken() {

    }

}