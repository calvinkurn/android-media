package com.tokopedia.tokomart.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutListUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokomart.home.presentation.uimodel.TokoMartHomeLayoutUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.async
import javax.inject.Inject

class TokoMartHomeViewModel @Inject constructor(
    private val getHomeLayoutListUseCase: GetHomeLayoutListUseCase,
    private val getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val homeLayoutList: LiveData<Result<List<Visitable<*>>>>
        get() = _homeLayoutList
    val homeLayoutData: LiveData<Result<List<Visitable<*>>>>
        get() = _homeLayoutData

    private val _homeLayoutList = MutableLiveData<Result<List<Visitable<*>>>>()
    private val _homeLayoutData = MutableLiveData<Result<List<Visitable<*>>>>()

    private var layoutList = listOf<Visitable<*>>()

    fun getHomeLayout() {
        launchCatchError(block = {
            val response = getHomeLayoutListUseCase.execute()
            layoutList = HomeLayoutMapper.mapHomeLayoutList(response)
            _homeLayoutList.postValue(Success(layoutList))
        }) {
            _homeLayoutList.postValue(Fail(it))
        }
    }

    fun getLayoutData() {
        launchCatchError(block = {
            layoutList.forEach { layout ->
                val getLayoutData = async {
                    val channelId = layout.getChannelId()
                    val response = getHomeLayoutDataUseCase.execute(channelId)
                    layoutList = HomeLayoutMapper.mapHomeLayoutData(layoutList, response)
                    layoutList
                }
                _homeLayoutData.postValue(Success(getLayoutData.await()))
            }
        }) {
            _homeLayoutData.postValue(Fail(it))
        }
    }

    private fun Visitable<*>.getChannelId(): String {
        return when (this) {
            is TokoMartHomeLayoutUiModel -> channelId
            is HomeComponentVisitable -> visitableId().orEmpty()
            else -> throw TypeNotSupportedException.create("Channel  Not Supported")
        }
    }
}