package com.tokopedia.ordermanagement.snapshot.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.ordermanagement.snapshot.data.model.GetOrderSnapshot
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotParam
import com.tokopedia.ordermanagement.snapshot.domain.GetImageUrlUseCase
import com.tokopedia.ordermanagement.snapshot.domain.SnapshotUseCase
import com.tokopedia.ordermanagement.snapshot.util.SnapshotIdlingResource
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by fwidjaja on 1/25/21.
 */
class SnapshotViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val snapshotUseCase: SnapshotUseCase,
    private val getImageUrlUseCase: GetImageUrlUseCase
) : BaseViewModel(dispatcher.main) {

    private val _snapshotResponse = MutableLiveData<Result<GetOrderSnapshot>>()
    val snapshotResponse: LiveData<Result<GetOrderSnapshot>>
        get() = _snapshotResponse

    fun loadSnapshot(paramSnapshot: SnapshotParam) {
        SnapshotIdlingResource.increment()
        launch(dispatcher.io, block = {
            _snapshotResponse.postValue(snapshotUseCase.executeSuspend(paramSnapshot))
        })
        SnapshotIdlingResource.decrement()
    }

    fun getImageUrls(images: List<String>) {
        launchCatchError(context = dispatcher.io, block = {
            val async = images.map {
                async {

                }
            }
        }, onError = {

        })
    }
}