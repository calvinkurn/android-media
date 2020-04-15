package com.rahullohra.fakeresponse.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahullohra.fakeresponse.domain.usecases.DownloadSqliteUseCase
import com.rahullohra.fakeresponse.domain.usecases.ShowRecordsUseCase
import com.rahullohra.fakeresponse.presentation.livedata.Fail
import com.rahullohra.fakeresponse.presentation.livedata.LiveDataResult
import com.rahullohra.fakeresponse.presentation.livedata.Success
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class FakeResponseVM constructor(
    val workerDispatcher: CoroutineDispatcher,
    val usecase: ShowRecordsUseCase,
    val sqliteUseCase: DownloadSqliteUseCase
) : ViewModel(), CoroutineScope {

    val clearSqlRecords = MutableLiveData<LiveDataResult<Boolean>>()
    val resetData = MutableLiveData<LiveDataResult<Boolean>>()
    private val ceh = CoroutineExceptionHandler { _, ex ->
        ex.printStackTrace()
    }

    override val coroutineContext: CoroutineContext
        get() = workerDispatcher + Job() + ceh

    fun deleteAllGqlRecords() {
        launch {
            try{

                usecase.deleteAllRecords()
                clearSqlRecords.postValue(Success(true))
            }catch (ex:Exception){
                clearSqlRecords.postValue(Fail(ex))
            }

        }
    }

    fun resetLibrary() {
        launch {
            try{
                usecase.deleteAllRecords()
                sqliteUseCase.deleteSqlite()
                resetData.postValue(Success(true))
            }catch (ex:Exception){
                resetData.postValue(Fail(ex))
            }

        }
    }
}