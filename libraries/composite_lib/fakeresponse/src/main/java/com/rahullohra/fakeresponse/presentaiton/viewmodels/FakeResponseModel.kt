package com.rahullohra.fakeresponse.presentaiton.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahullohra.fakeresponse.ResponseItemType
import com.rahullohra.fakeresponse.ResponseListData
import com.rahullohra.fakeresponse.domain.usecases.ShowRecordsUseCase
import com.rahullohra.fakeresponse.domain.usecases.UpdateGqlUseCase
import com.rahullohra.fakeresponse.presentaiton.livedata.Fail
import com.rahullohra.fakeresponse.presentaiton.livedata.LiveDataResult
import com.rahullohra.fakeresponse.presentaiton.livedata.Success
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
