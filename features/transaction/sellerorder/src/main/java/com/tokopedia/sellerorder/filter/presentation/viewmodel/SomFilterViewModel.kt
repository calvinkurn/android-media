package com.tokopedia.sellerorder.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.filter.domain.usecase.GetSomOrderFilterUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SomFilterViewModel @Inject constructor(dispatcher: SomDispatcherProvider,
                                            private val getSomOrderFilterUseCase: GetSomOrderFilterUseCase): BaseViewModel(dispatcher.io()) {

    private val _filterResult = MutableLiveData<Result<SomFilterUiModel>>()
    val filterResult: LiveData<Result<SomFilterUiModel>> = _filterResult

    fun getSomFilterData() {
        launchCatchError(block = {
            val result = getSomOrderFilterUseCase.execute()
            _filterResult.postValue(Success(result))
        }, onError = {
            _filterResult.postValue(Fail(it))
        })
    }
}