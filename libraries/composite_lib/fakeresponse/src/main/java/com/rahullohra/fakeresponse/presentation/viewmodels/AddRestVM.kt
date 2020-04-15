package com.rahullohra.fakeresponse.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahullohra.fakeresponse.chuck.TransactionEntity
import com.rahullohra.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.rahullohra.fakeresponse.db.entities.RestRecord
import com.rahullohra.fakeresponse.domain.usecases.AddRestDaoUseCase
import com.rahullohra.fakeresponse.presentation.livedata.Fail
import com.rahullohra.fakeresponse.presentation.livedata.LiveDataResult
import com.rahullohra.fakeresponse.presentation.livedata.Success
import com.rahullohra.fakeresponse.presentation.viewmodels.data.AddRestData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddRestVM constructor(
        val workerDispatcher: CoroutineDispatcher,
        val usecase: AddRestDaoUseCase,
        val chuckSearchUseCase: ChuckSearchUseCase
) : ViewModel(), CoroutineScope {

    val liveDataCreate = MutableLiveData<LiveDataResult<Long>>()
    val liveDataUpdate = MutableLiveData<LiveDataResult<Boolean>>()
    val liveDataRestResponse = MutableLiveData<LiveDataResult<RestRecord>>()
    val liveDataTransactionEntity = MutableLiveData<LiveDataResult<TransactionEntity>>()

    override val coroutineContext: CoroutineContext
        get() = workerDispatcher + ceh

    private val ceh = CoroutineExceptionHandler { _, ex ->
        liveDataCreate.postValue(Fail(ex))
        ex.printStackTrace()
    }

    fun addRecord(data: AddRestData) {
        launch {
            try {
                val rowId = usecase.addRestRecord(data)
                liveDataCreate.postValue(Success(rowId))
            } catch (ex: Exception) {
                liveDataCreate.postValue(Fail(ex))
            }
        }
    }

    fun updateRecord(id: Int, data: AddRestData) {
        launch {
            try {
                usecase.updateRestRecord(id, data)
                liveDataUpdate.postValue(Success(true))
            } catch (ex: Exception) {
                liveDataUpdate.postValue(Fail(ex))
            }

        }
    }

    fun loadRecord(id: Int) {
        launch {
            try {
                val result = usecase.getRecordFromTable(id)
                liveDataRestResponse.postValue(Success(result))
            } catch (ex: Exception) {
                liveDataRestResponse.postValue(Fail(ex))
            }

        }
    }

    fun loadRecordFromChuck(id: Int) {
        launch {
            try {
                val result = chuckSearchUseCase.performSearch(id = id.toLong()).first()
                liveDataTransactionEntity.postValue(Success(result))
            } catch (ex: Exception) {
                liveDataTransactionEntity.postValue(Fail(ex))
            }
        }
    }
}