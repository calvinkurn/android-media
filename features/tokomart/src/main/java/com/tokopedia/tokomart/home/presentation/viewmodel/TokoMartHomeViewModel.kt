package com.tokopedia.tokomart.home.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutUseCase
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.mapToHomeUiModel
import com.tokopedia.tokomart.home.domain.model.SearchPlaceholder
import com.tokopedia.tokomart.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class TokoMartHomeViewModel @Inject constructor(
    private val getHomeLayoutUseCase: GetHomeLayoutUseCase,
    private val getKeywordSearchUseCase: GetKeywordSearchUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io) {

    val homeLayout: LiveData<Result<List<Visitable<*>>>>
        get() = _homeLayout

    val searchHint: LiveData<SearchPlaceholder>
        get() = _searchHint

    private val _homeLayout = MutableLiveData<Result<List<Visitable<*>>>>()
    private val _searchHint = MutableLiveData<SearchPlaceholder>()

    fun getHomeLayout() {
       launchCatchError(block = {
           val response = getHomeLayoutUseCase.execute()
           val data = mapToHomeUiModel(response)
           _homeLayout.postValue(Success(data))
       }) {
           _homeLayout.postValue(Fail(it))
       }
    }

    fun getSearchHint(isFirstInstall: Boolean, deviceId: String, userId: String) {
        launchCatchError(coroutineContext, block = {
            getKeywordSearchUseCase.params = getKeywordSearchUseCase.createParams(isFirstInstall, deviceId, userId)
            val data = getKeywordSearchUseCase.executeOnBackground()
            _searchHint.postValue(data.searchData)
        }) {}
    }
}