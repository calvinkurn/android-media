package com.rahullohra.fakeresponse.presentaiton.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahullohra.fakeresponse.domain.usecases.AddToDbUseCase
import com.rahullohra.fakeresponse.presentaiton.livedata.Fail
import com.rahullohra.fakeresponse.presentaiton.livedata.LiveDataResult
import com.rahullohra.fakeresponse.presentaiton.livedata.Success
import com.rahullohra.fakeresponse.presentaiton.viewmodels.data.AddGqlData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddGqlVM constructor(
    val uiDispatcher: CoroutineDispatcher,
    val workerDispatcher: CoroutineDispatcher,
    val addToDbUseCase: AddToDbUseCase
) : ViewModel(), CoroutineScope {

    val liveData = MutableLiveData<LiveDataResult<Long>>()

    override val coroutineContext: CoroutineContext
        get() = workerDispatcher + ceh

    private val ceh = CoroutineExceptionHandler { _, ex ->
        liveData.postValue(Fail(ex))
        ex.printStackTrace()
    }

    fun addToDb(data: AddGqlData) {
        launch {
            val rowId = addToDbUseCase.addToDb(data)
            liveData.postValue(Success(rowId))
        }
    }

}