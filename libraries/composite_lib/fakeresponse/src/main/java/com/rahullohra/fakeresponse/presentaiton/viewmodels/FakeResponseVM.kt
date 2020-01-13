package com.rahullohra.fakeresponse.presentaiton.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahullohra.fakeresponse.domain.usecases.DownloadSqliteUseCase
import com.rahullohra.fakeresponse.domain.usecases.ShowGqlUseCase
import com.rahullohra.fakeresponse.presentaiton.livedata.Fail
import com.rahullohra.fakeresponse.presentaiton.livedata.LiveDataResult
import com.rahullohra.fakeresponse.presentaiton.livedata.Success
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class FakeResponseVM constructor(
    val workerDispatcher: CoroutineDispatcher,
    val usecase: ShowGqlUseCase,
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