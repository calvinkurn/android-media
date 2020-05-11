package com.tokopedia.fakeresponse.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.fakeresponse.data.models.ResponseListData
import com.tokopedia.fakeresponse.domain.usecases.ShowRecordsUseCase
import com.tokopedia.fakeresponse.domain.usecases.UpdateGqlUseCase
import com.tokopedia.fakeresponse.presentation.livedata.Fail
import com.tokopedia.fakeresponse.presentation.livedata.LiveDataResult
import com.tokopedia.fakeresponse.presentation.livedata.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FakeResponseModel constructor(
    val workerDispatcher: CoroutineDispatcher,
    val showRecordsUseCase: ShowRecordsUseCase,
    val updateGqlUseCase: UpdateGqlUseCase

) : ViewModel(), CoroutineScope {

    val liveData = MutableLiveData<LiveDataResult<List<ResponseListData>>>()
    val toggleLiveData = MutableLiveData<LiveDataResult<Pair<Int,Boolean>>>()

    override val coroutineContext: CoroutineContext
        get() = workerDispatcher + ceh

    private val ceh = CoroutineExceptionHandler { _, ex ->
        liveData.postValue(Fail(ex))
        ex.printStackTrace()
    }

    fun toggleGql(data: ResponseListData, isEnabled: Boolean) {
        launch {
            try {
                updateGqlUseCase.toggle(data.id, isEnabled, data.responseType)
                toggleLiveData.postValue(Success(Pair(data.id, isEnabled)))
            } catch (ex: Exception) {
                toggleLiveData.postValue(Fail(ex))
                toggleLiveData.postValue(Success(Pair(data.id, !isEnabled)))
            }
        }

    }

    fun getGql() {
        launch {
            liveData.postValue(Success(showRecordsUseCase.getAllQueries()))
        }
    }

}
