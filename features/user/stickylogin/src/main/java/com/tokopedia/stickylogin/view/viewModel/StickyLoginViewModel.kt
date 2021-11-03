package com.tokopedia.stickylogin.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.stickylogin.common.StickyLoginConstant
import com.tokopedia.stickylogin.domain.data.StickyLoginTickerDataModel
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
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

            val response = stickyLoginUseCase(mutableMapOf(
                StickyLoginConstant.PARAMS_PAGE to page.toString()
            )).response.tickerDataModels.filter {
                it.layout == StickyLoginConstant.LAYOUT_FLOATING
            }

            if (response.isNotEmpty()) {
                _stickyContent.value = Success(StickyLoginTickerDataModel(response))
            } else {
                _stickyContent.value = Fail(Throwable(ERROR_DATA_NOT_FOUND))
            }
        }, {
            _stickyContent.postValue(Fail(it))
        })
    }

    companion object {
        const val ERROR_DATA_NOT_FOUND = "data not found"
    }
}