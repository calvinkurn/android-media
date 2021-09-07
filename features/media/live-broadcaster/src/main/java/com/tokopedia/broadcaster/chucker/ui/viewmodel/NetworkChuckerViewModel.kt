package com.tokopedia.broadcaster.chucker.ui.viewmodel

import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.broadcaster.chucker.data.mapper.mapToUI
import com.tokopedia.broadcaster.chucker.data.repository.ChuckerLogRepository
import com.tokopedia.broadcaster.chucker.ui.uimodel.ChuckerLogUIModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkChuckerViewModel @Inject constructor(
    private val repository: ChuckerLogRepository,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher), LifecycleObserver {

    private val _chuckers = MutableLiveData<MutableList<ChuckerLogUIModel>>()
    val chuckers: LiveData<MutableList<ChuckerLogUIModel>> get() = _chuckers

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