package com.tokopedia.tokomart.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper
import com.tokopedia.tokomart.home.domain.mapper.HomeLayoutMapper.ADDITIONAL_WIDGETS
import com.tokopedia.tokomart.home.domain.model.SearchPlaceholder
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutListUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.tokomart.home.domain.usecase.GetKeywordSearchUseCase
import com.tokopedia.tokomart.home.presentation.uimodel.TokoMartHomeLayoutUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.async
import javax.inject.Inject

class TokoMartHomeViewModel @Inject constructor(
        private val getHomeLayoutListUseCase: GetHomeLayoutListUseCase,
        private val getHomeLayoutDataUseCase: GetHomeLayoutDataUseCase,
        private val getKeywordSearchUseCase: GetKeywordSearchUseCase,
        private val getNotificationUseCase: GetNotificationUseCase,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val homeLayoutList: LiveData<Result<List<Visitable<*>>>>
        get() = _homeLayoutList
    val homeLayoutData: LiveData<Result<List<Visitable<*>>>>
        get() = _homeLayoutData
    val searchHint: LiveData<SearchPlaceholder>
        get() = _searchHint
    val notificationCounter: LiveData<TopNavNotificationModel>
        get() = _notificationCounter

    private val _homeLayoutList = MutableLiveData<Result<List<Visitable<*>>>>()
    private val _homeLayoutData = MutableLiveData<Result<List<Visitable<*>>>>()
    private val _searchHint = MutableLiveData<SearchPlaceholder>()
    private val _notificationCounter = MutableLiveData<TopNavNotificationModel>()


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
                if (layout.getChannelId() in ADDITIONAL_WIDGETS) {
                    return@forEach
                }
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

    fun getSearchHint(isFirstInstall: Boolean, deviceId: String, userId: String) {
        launchCatchError(coroutineContext, block = {
            getKeywordSearchUseCase.params = getKeywordSearchUseCase.createParams(isFirstInstall, deviceId, userId)
            val data = getKeywordSearchUseCase.executeOnBackground()
            _searchHint.postValue(data.searchData)
        }) {}
    }

    // only used to count the old toolbar's notif
    fun getNotification() {
        launchCatchError(coroutineContext, block = {
            val topNavNotificationModel = getNotificationUseCase.executeOnBackground()
            _notificationCounter.postValue(topNavNotificationModel)
        }) {}
    }

    private fun Visitable<*>.getChannelId(): String {
        return when (this) {
            is TokoMartHomeLayoutUiModel -> channelId
            is HomeComponentVisitable -> visitableId().orEmpty()
            else -> throw TypeNotSupportedException.create("Channel  Not Supported")
        }
    }
}