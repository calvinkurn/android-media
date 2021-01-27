package com.tokopedia.ordermanagement.snapshot.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotParam
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotResponse
import com.tokopedia.ordermanagement.snapshot.domain.SnapshotUseCase
import com.tokopedia.ordermanagement.snapshot.util.SnapshotDispatcherProvider
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by fwidjaja on 1/25/21.
 */
class SnapshotViewModel @Inject constructor(dispatcher: SnapshotDispatcherProvider,
                                            private val snapshotUseCase: SnapshotUseCase) : BaseViewModel(dispatcher.ui()) {

    private val _snapshotResponse = MutableLiveData<Result<SnapshotResponse.Data.GetOrderSnapshot>>()
    val snapshotResponse: LiveData<Result<SnapshotResponse.Data.GetOrderSnapshot>>
        get() = _snapshotResponse

    fun loadSnapshot(paramSnapshot: SnapshotParam) {
        launch {
            _snapshotResponse.value = snapshotUseCase.executeSuspend(paramSnapshot)
        }
    }
}