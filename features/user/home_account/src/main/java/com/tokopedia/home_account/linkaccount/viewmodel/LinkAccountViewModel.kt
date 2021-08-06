package com.tokopedia.home_account.linkaccount.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.linkaccount.data.LinkStatusResponse
import com.tokopedia.home_account.linkaccount.domain.GetLinkStatusUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Yoris on 04/08/21.
 */

class LinkAccountViewModel @Inject constructor(
    private val getLinkStatusUseCase: GetLinkStatusUseCase,
    private val dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.io), LifecycleObserver {

    private val _linkStatus = MutableLiveData<Result<LinkStatusResponse>>()
    val linkStatus: LiveData<Result<LinkStatusResponse>> get() = _linkStatus

    fun getLinkStatus() {
        launchCatchError(block = {
            val result = getLinkStatusUseCase(RequestParams.EMPTY)
            withContext(dispatcher.main) {
                _linkStatus.value = Success(result)
            }
        }, onError = {
            _linkStatus.postValue(Fail(it))
        })
    }
}