package com.rahullohra.fakeresponse.chuck.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahullohra.fakeresponse.data.models.ResponseListData
import com.rahullohra.fakeresponse.chuck.ChuckDBConnector
import com.rahullohra.fakeresponse.chuck.TransactionEntity
import com.rahullohra.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.rahullohra.fakeresponse.domain.usecases.ShowRecordsUseCase
import com.rahullohra.fakeresponse.presentaiton.livedata.Fail
import com.rahullohra.fakeresponse.presentaiton.livedata.LiveDataResult
import com.rahullohra.fakeresponse.presentaiton.livedata.Success
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchViewModel(val workerDispatcher: CoroutineDispatcher,
                      val usecase: ChuckSearchUseCase,
                      val recordsUseCase: ShowRecordsUseCase) : ViewModel(), CoroutineScope {

    val searchLiveData = MutableLiveData<LiveDataResult<Pair<List<TransactionEntity>, List<ResponseListData>>>>()

    override val coroutineContext: CoroutineContext
        get() = workerDispatcher

    fun search(responseBody: String?, url: String?, requestBody: String?, tag: String?) {
        launch {
            try {
                val list = usecase.performSearch(requestBody = requestBody, responseBody = responseBody, url = url)
                val responseListData = recordsUseCase.search(url = url, tag = tag, response = responseBody)
                val pair = Pair(list,responseListData)
                searchLiveData.postValue(Success(pair))
            } catch (ex: Exception) {
                ex.printStackTrace()
                searchLiveData.postValue(Fail(ex))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (coroutineContext.isActive) {
            coroutineContext.cancel()
        }
        ChuckDBConnector.close()
    }
}