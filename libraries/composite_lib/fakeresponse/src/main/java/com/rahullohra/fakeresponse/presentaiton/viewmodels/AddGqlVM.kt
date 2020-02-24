package com.rahullohra.fakeresponse.presentaiton.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahullohra.fakeresponse.db.entities.GqlRecord
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
        val workerDispatcher: CoroutineDispatcher,
        val useCase: AddToDbUseCase
) : ViewModel(), CoroutineScope {

    val liveDataCreate = MutableLiveData<LiveDataResult<Long>>()
    val liveDataGqlResponse = MutableLiveData<LiveDataResult<GqlRecord>>()
    val liveDataGqlUpdate = MutableLiveData<LiveDataResult<Boolean>>()

    override val coroutineContext: CoroutineContext
        get() = workerDispatcher + ceh

    private val ceh = CoroutineExceptionHandler { _, ex ->
        liveDataCreate.postValue(Fail(ex))
        ex.printStackTrace()
    }

    fun addToDb(data: AddGqlData) {
        launch {
            val rowId = useCase.addToDb(data)
            liveDataCreate.postValue(Success(rowId))
        }
    }

    fun updateRecord(id:Int, data: AddGqlData) {
        launch {
            useCase.updateRestRecord(id, data)
            liveDataGqlUpdate.postValue(Success(true))
        }
    }

    fun loadRecord(id: Int) {
        launch {
            val result = useCase.getRecordFromTable(id)
            liveDataGqlResponse.postValue(Success(result))
        }
    }
}