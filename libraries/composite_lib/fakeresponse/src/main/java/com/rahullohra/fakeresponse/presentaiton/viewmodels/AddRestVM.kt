package com.rahullohra.fakeresponse.presentaiton.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahullohra.fakeresponse.db.entities.RestResponse
import com.rahullohra.fakeresponse.domain.usecases.AddRestDaoUseCase
import com.rahullohra.fakeresponse.presentaiton.livedata.Fail
import com.rahullohra.fakeresponse.presentaiton.livedata.LiveDataResult
import com.rahullohra.fakeresponse.presentaiton.livedata.Success
import com.rahullohra.fakeresponse.presentaiton.viewmodels.data.AddRestData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddRestVM constructor(
        val workerDispatcher: CoroutineDispatcher,
        val usecase: AddRestDaoUseCase
) : ViewModel(), CoroutineScope {

    val liveDataCreate = MutableLiveData<LiveDataResult<Long>>()
    val liveDataUpdate = MutableLiveData<LiveDataResult<Boolean>>()
    val liveDataRestResponse = MutableLiveData<LiveDataResult<RestResponse>>()

    override val coroutineContext: CoroutineContext
        get() = workerDispatcher + ceh

    private val ceh = CoroutineExceptionHandler { _, ex ->
        liveDataCreate.postValue(Fail(ex))
        ex.printStackTrace()
    }

    fun addRecord(data: AddRestData) {
        launch {
            val rowId = usecase.addRestRecord(data)
            liveDataCreate.postValue(Success(rowId))
        }
    }

    fun updateRecord(id:Int, data: AddRestData) {
        launch {
            usecase.updateRestRecord(id, data)
            liveDataUpdate.postValue(Success(true))
        }
    }

    fun loadRecord(id: Int) {
        launch {
            val result = usecase.getRecordFromTable(id)
            liveDataRestResponse.postValue(Success(result))
        }
    }

}