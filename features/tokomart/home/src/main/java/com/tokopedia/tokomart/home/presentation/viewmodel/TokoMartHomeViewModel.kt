package com.tokopedia.tokomart.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokomart.home.base.uimodel.BaseHomeUiModel
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutUseCase
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapToHomeUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class TokoMartHomeViewModel @Inject constructor(
    private val getHomeLayoutUseCase: GetHomeLayoutUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    val homeLayout: LiveData<Result<List<BaseHomeUiModel>>>
        get() = _homeLayout

    private val _homeLayout = MutableLiveData<Result<List<BaseHomeUiModel>>>()

    fun getHomeLayout() {
       launchCatchError(block = {
           val response = getHomeLayoutUseCase.execute()
           val data = mapToHomeUiModel(response)
           _homeLayout.postValue(Success(data))
       }) {
           _homeLayout.postValue(Fail(it))
       }
    }
}