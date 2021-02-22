package com.tokopedia.ordermanagement.snapshot.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.ordermanagement.snapshot.data.model.GetOrderSnapshot
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotParam
import com.tokopedia.ordermanagement.snapshot.domain.SnapshotUseCase
import com.tokopedia.ordermanagement.snapshot.util.SnapshotDispatcherProvider
import com.tokopedia.ordermanagement.snapshot.util.SnapshotIdlingResource
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by fwidjaja on 1/25/21.
 */
class SnapshotViewModel @Inject constructor(dispatcher: SnapshotDispatcherProvider,
                                            private val snapshotUseCase: SnapshotUseCase) : BaseViewModel(dispatcher.io()) {

    private val _snapshotResponse = MutableLiveData<Result<GetOrderSnapshot>>()
    val snapshotResponse: LiveData<Result<GetOrderSnapshot>>
        get() = _snapshotResponse

    fun loadSnapshot(paramSnapshot: SnapshotParam) {
        SnapshotIdlingResource.increment()
        launch {
            _snapshotResponse.postValue(snapshotUseCase.executeSuspend(paramSnapshot))
            SnapshotIdlingResource.decrement()
        }
    }
}