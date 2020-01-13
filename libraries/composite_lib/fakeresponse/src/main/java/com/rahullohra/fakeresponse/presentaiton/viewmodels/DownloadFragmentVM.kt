package com.rahullohra.fakeresponse.presentaiton.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahullohra.fakeresponse.domain.usecases.DownloadSqliteUseCase
import com.rahullohra.fakeresponse.presentaiton.livedata.Fail
import com.rahullohra.fakeresponse.presentaiton.livedata.LiveDataResult
import com.rahullohra.fakeresponse.presentaiton.livedata.Loading
import com.rahullohra.fakeresponse.presentaiton.livedata.Success
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DownloadFragmentVM  constructor(
    val uiDispatcher: CoroutineDispatcher,
    val workerDispatcher: CoroutineDispatcher,
    val usecase: DownloadSqliteUseCase
) : ViewModel(), CoroutineScope {

    val liveData = MutableLiveData<LiveDataResult<Boolean>>()
    private val ceh = CoroutineExceptionHandler { _, ex ->
        liveData.postValue(Fail(ex))
        ex.printStackTrace()
    }

    override val coroutineContext: CoroutineContext
        get() = workerDispatcher + Job() + ceh

    fun downloadSqliteFiles() {
        launch {
            liveData.postValue(Loading())
            usecase.getSqlite()
            liveData.postValue(Success(true))
        }
    }
}