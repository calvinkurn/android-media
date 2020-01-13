package com.rahullohra.fakeresponse.presentaiton.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahullohra.fakeresponse.ResponseListData
import com.rahullohra.fakeresponse.domain.usecases.ShowGqlUseCase
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
    val showGqlUseCase: ShowGqlUseCase,
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

    fun toggleGql(id: Int, isEnabled: Boolean) {
        if(true)return
        launch {
            try {
                updateGqlUseCase.toggleGql(id, isEnabled)
                toggleLiveData.postValue(Success(Pair(id, isEnabled)))
            } catch (ex: Exception) {
                toggleLiveData.postValue(Fail(ex))
                toggleLiveData.postValue(Success(Pair(id, !isEnabled)))
            }
        }

    }

    fun getGql() {
        launch {
            liveData.postValue(Success(showGqlUseCase.getAllQueries()))
        }
    }

}
