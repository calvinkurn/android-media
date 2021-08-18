package com.tokopedia.broadcaster.chucker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.broadcaster.chucker.data.mapper.mapToData
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
    dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private var _broadcasterLog = MutableLiveData<List<ChuckerLogUIModel>>()
    val broadcasterLog: LiveData<List<ChuckerLogUIModel>> get() = _broadcasterLog

    init {
        chucker()
    }

    fun chucker() {
        launch {
            val result = repository.chuckers()
            withContext(Dispatchers.Main) {
                _broadcasterLog.value = result.asReversed().mapToUI()
            }
        }
    }

    fun log(model: ChuckerLogUIModel) {
        launch {
            repository.logChucker(model.mapToData())
        }
    }

}