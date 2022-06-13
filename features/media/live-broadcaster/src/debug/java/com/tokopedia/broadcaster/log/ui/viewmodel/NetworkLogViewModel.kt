package com.tokopedia.broadcaster.log.ui.viewmodel

import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.broadcaster.log.data.mapper.mapToUI
import com.tokopedia.broadcaster.log.data.repository.ChuckerLogRepository
import com.tokopedia.broadcaster.data.uimodel.LoggerUIModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkLogViewModel @Inject constructor(
    private val repository: ChuckerLogRepository,
    dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher), LifecycleObserver {

    private val _chuckers = MutableLiveData<MutableList<LoggerUIModel>>()
    val chuckers: LiveData<MutableList<LoggerUIModel>> get() = _chuckers

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getChuckers() {
        launch {
            val result = repository.chuckers()
            withContext(Dispatchers.Main) {
                _chuckers.value = result.reversed().mapToUI()
            }
        }
    }

}