package com.tokopedia.fakeresponse.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.fakeresponse.domain.usecases.ImportUseCase
import com.tokopedia.fakeresponse.presentation.livedata.Fail
import com.tokopedia.fakeresponse.presentation.livedata.LiveDataResult
import com.tokopedia.fakeresponse.presentation.livedata.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PasteTextViewModel(val workerDispatcher: CoroutineDispatcher,
                         val importUseCase: ImportUseCase) : ViewModel(), CoroutineScope {

    val importLiveData = MutableLiveData<LiveDataResult<Boolean>>()
    override val coroutineContext: CoroutineContext
        get() = workerDispatcher

    fun performImport(string: String) {
        launch {
            try {
                importUseCase.importText(string)
                importLiveData.postValue(Success(true))
            } catch (ex: Exception) {
                importLiveData.postValue(Fail(ex))
            }

        }

    }

}