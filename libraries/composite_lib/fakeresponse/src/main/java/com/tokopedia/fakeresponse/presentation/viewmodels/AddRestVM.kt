package com.tokopedia.fakeresponse.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.fakeresponse.chuck.TransactionEntity
import com.tokopedia.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.tokopedia.fakeresponse.data.models.ResponseItemType
import com.tokopedia.fakeresponse.db.entities.RestRecord
import com.tokopedia.fakeresponse.domain.usecases.AddRestDaoUseCase
import com.tokopedia.fakeresponse.domain.usecases.ExportUseCase
import com.tokopedia.fakeresponse.presentation.livedata.Fail
import com.tokopedia.fakeresponse.presentation.livedata.LiveDataResult
import com.tokopedia.fakeresponse.presentation.livedata.Success
import com.tokopedia.fakeresponse.presentation.viewmodels.data.AddRestData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddRestVM constructor(
        val workerDispatcher: CoroutineDispatcher,
        val usecase: AddRestDaoUseCase,
        val chuckSearchUseCase: ChuckSearchUseCase,
        val exportUseCase: ExportUseCase
) : ViewModel(), CoroutineScope {

    val liveDataCreate = MutableLiveData<LiveDataResult<Long>>()
    val liveDataUpdate = MutableLiveData<LiveDataResult<Boolean>>()
    val liveDataRestResponse = MutableLiveData<LiveDataResult<RestRecord>>()
    val liveDataTransactionEntity = MutableLiveData<LiveDataResult<TransactionEntity>>()
    val liveDataExport = MutableLiveData<LiveDataResult<String>>()
    val liveDataDeleteRecord = MutableLiveData<LiveDataResult<Boolean>>()

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

    fun export(id: Int, responseItemType: ResponseItemType) {
        launch {
            try {
                val text = exportUseCase.export(id, responseItemType)
                liveDataExport.postValue(Success(text))
            } catch (ex: Exception) {
                liveDataExport.postValue(Fail(ex))
            }
        }
    }

    fun delete(id: Int) {
        launch {
            try {
                usecase.deleteRecord(id)
                liveDataDeleteRecord.postValue(Success(true))
            } catch (ex: Exception) {
                liveDataDeleteRecord.postValue(Fail(ex))
            }
        }
    }
}