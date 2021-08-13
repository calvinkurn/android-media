package com.tokopedia.stickylogin.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.stickylogin.common.StickyLoginConstant
import com.tokopedia.stickylogin.domain.data.StickyLoginTickerDataModel
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class StickyLoginViewModel @Inject constructor(
        private val stickyLoginUseCase: StickyLoginUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _stickyContent = MutableLiveData<Result<StickyLoginTickerDataModel>>()
    val stickyContent: LiveData<Result<StickyLoginTickerDataModel>>
        get() = _stickyContent

    fun getStickyContent(page: StickyLoginConstant.Page) {
        launchCatchError(coroutineContext, {
            stickyLoginUseCase.setParam(generateParams(page))
            stickyLoginUseCase.execute(onSuccess = {
                if (it.response.tickerDataModels.isNotEmpty() && it.response.tickerDataModels[0].layout == StickyLoginConstant.LAYOUT_FLOATING) {
                    _stickyContent.postValue(Success(it.response))
                } else {
                    _stickyContent.postValue(Fail(Throwable(ERROR_DATA_NOT_FOUND)))
                }
            }, onError = {
                _stickyContent.postValue(Fail(it))
            })
        }, {
            _stickyContent.postValue(Fail(it))
        })
    }

    fun generateParams(page: StickyLoginConstant.Page): RequestParams {
        return RequestParams.create().apply {
            putString(StickyLoginConstant.PARAMS_PAGE, page.toString())
        }
    }

    public override fun onCleared() {
        super.onCleared()
        stickyLoginUseCase.cancelJobs()
    }

    companion object {
        const val ERROR_DATA_NOT_FOUND = "data not found"
    }
}