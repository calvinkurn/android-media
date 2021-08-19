package com.tokopedia.broadcaster.chucker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.broadcaster.chucker.data.mapper.mapToUI
import com.tokopedia.broadcaster.chucker.data.repository.ChuckerLogRepository
import com.tokopedia.broadcaster.chucker.ui.uimodel.ChuckerLogUIModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class NetworkChuckerViewModel @Inject constructor(
    repository: ChuckerLogRepository,
    dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    var broadcasterLog: LiveData<MutableList<ChuckerLogUIModel>>
    = Transformations.map(repository.chuckers()) {
        it.asReversed().mapToUI()
    }

}