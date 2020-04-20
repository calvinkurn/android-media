package com.tokopedia.fakeresponse.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.fakeresponse.chuck.TransactionEntity
import com.tokopedia.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.tokopedia.fakeresponse.data.models.ResponseItemType
import com.tokopedia.fakeresponse.db.entities.GqlRecord
import com.tokopedia.fakeresponse.domain.usecases.AddToDbUseCase
import com.tokopedia.fakeresponse.domain.usecases.ExportUseCase
import com.tokopedia.fakeresponse.presentation.livedata.Fail
import com.tokopedia.fakeresponse.presentation.livedata.LiveDataResult
import com.tokopedia.fakeresponse.presentation.livedata.Success
import com.tokopedia.fakeresponse.presentation.viewmodels.data.AddGqlData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddGqlVM constructor(
        val workerDispatcher: CoroutineDispatcher,
        val useCase: AddToDbUseCase,
        val chuckSearchUseCase: ChuckSearchUseCase,
        val exportUseCase: ExportUseCase
) : ViewModel(), CoroutineScope {

    val liveDataCreate = MutableLiveData<LiveDataResult<Long>>()
    val liveDataGqlResponse = MutableLiveData<LiveDataResult<GqlRecord>>()
    val liveDataTransactionEntity = MutableLiveData<LiveDataResult<TransactionEntity>>()
    val liveDataGqlUpdate = MutableLiveData<LiveDataResult<Boolean>>()
    val liveDataExport = MutableLiveData<LiveDataResult<String>>()

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
                useCase.updateRecord(id, data)
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
}