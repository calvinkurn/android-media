package com.rahullohra.fakeresponse.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahullohra.fakeresponse.chuck.TransactionEntity
import com.rahullohra.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.rahullohra.fakeresponse.db.entities.GqlRecord
import com.rahullohra.fakeresponse.domain.usecases.AddToDbUseCase
import com.rahullohra.fakeresponse.presentation.livedata.Fail
import com.rahullohra.fakeresponse.presentation.livedata.LiveDataResult
import com.rahullohra.fakeresponse.presentation.livedata.Success
import com.rahullohra.fakeresponse.presentation.viewmodels.data.AddGqlData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddGqlVM constructor(
        val workerDispatcher: CoroutineDispatcher,
        val useCase: AddToDbUseCase,
        val chuckSearchUseCase: ChuckSearchUseCase
) : ViewModel(), CoroutineScope {

    val liveDataCreate = MutableLiveData<LiveDataResult<Long>>()
    val liveDataGqlResponse = MutableLiveData<LiveDataResult<GqlRecord>>()
    val liveDataTransactionEntity = MutableLiveData<LiveDataResult<TransactionEntity>>()
    val liveDataGqlUpdate = MutableLiveData<LiveDataResult<Boolean>>()

    override val coroutineContext: CoroutineContext
        get() = workerDispatcher + ceh

    private val ceh = CoroutineExceptionHandler { _, ex ->
        ex.printStackTrace()
    }

    fun addToDb(data: AddGqlData) {
        launch {
            try {
                val rowId = useCase.addToDb(data)
                liveDataCreate.postValue(Success(rowId))
            } catch (ex: Exception) {
                ex.printStackTrace()
                liveDataCreate.postValue(Fail(ex))
            }

        }
    }

    fun updateRecord(id: Int, data: AddGqlData) {
        launch {
            try {
                useCase.updateRestRecord(id, data)
                liveDataGqlUpdate.postValue(Success(true))
            } catch (ex: Exception) {
                ex.printStackTrace()
                liveDataGqlUpdate.postValue(Fail(ex))
            }
        }
    }

    fun loadRecord(id: Int) {
        launch {
            try {
                val result = useCase.getRecordFromTable(id)
                liveDataGqlResponse.postValue(Success(result))
            } catch (ex: Exception) {
                ex.printStackTrace()
                liveDataGqlResponse.postValue(Fail(ex))
            }
        }
    }

    fun loadRecordFromChuck(id: Int) {
        launch {
            try {
                val result = chuckSearchUseCase.performSearch(id = id.toLong()).first()
                liveDataTransactionEntity.postValue(Success(result))
            } catch (ex: Exception) {
                ex.printStackTrace()
                liveDataTransactionEntity.postValue(Fail(ex))
            }
        }
    }
}