package com.tokopedia.fakeresponse.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.fakeresponse.domain.usecases.DownloadSqliteUseCase
import com.tokopedia.fakeresponse.presentation.livedata.Fail
import com.tokopedia.fakeresponse.presentation.livedata.LiveDataResult
import com.tokopedia.fakeresponse.presentation.livedata.Loading
import com.tokopedia.fakeresponse.presentation.livedata.Success
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