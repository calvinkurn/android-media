package com.tokopedia.broadcaster.statsnerd.ui.viewmodel

import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.broadcaster.statsnerd.data.mapper.mapToUI
import com.tokopedia.broadcaster.statsnerd.data.repository.ChuckerLogRepository
import com.tokopedia.broadcaster.uimodel.LoggerUIModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkStatsNerdViewModel @Inject constructor(
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