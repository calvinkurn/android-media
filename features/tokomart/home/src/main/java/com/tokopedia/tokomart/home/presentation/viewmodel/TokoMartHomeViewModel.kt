package com.tokopedia.tokomart.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutUseCase
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapToSectionUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSectionUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class TokoMartHomeViewModel @Inject constructor(
    private val getHomeLayoutUseCase: GetHomeLayoutUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    val homeLayout: LiveData<Result<List<HomeSectionUiModel>>>
        get() = _homeLayout

    private val _homeLayout = MutableLiveData<Result<List<HomeSectionUiModel>>>()

    fun getHomeLayout() {
       launchCatchError(block = {
           val response = getHomeLayoutUseCase.execute()
           val data = mapToSectionUiModel(response)
           _homeLayout.postValue(Success(data))
       }) {
           _homeLayout.postValue(Fail(it))
       }
    }
}