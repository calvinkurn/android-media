package com.tokopedia.fakeresponse.chuck.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.fakeresponse.GQL_RECORD
import com.tokopedia.fakeresponse.REST_RECORD
import com.tokopedia.fakeresponse.TRANSACTION
import com.tokopedia.fakeresponse.chuck.ChuckDBConnector
import com.tokopedia.fakeresponse.chuck.TransactionEntity
import com.tokopedia.fakeresponse.chuck.domain.usecase.ChuckSearchUseCase
import com.tokopedia.fakeresponse.data.models.ResponseItemType
import com.tokopedia.fakeresponse.data.models.ResponseListData
import com.tokopedia.fakeresponse.data.models.SearchType
import com.tokopedia.fakeresponse.db.entities.GqlRecord
import com.tokopedia.fakeresponse.db.entities.RestRecord
import com.tokopedia.fakeresponse.domain.usecases.ShowRecordsUseCase
import com.tokopedia.fakeresponse.presentation.livedata.Fail
import com.tokopedia.fakeresponse.presentation.livedata.LiveDataResult
import com.tokopedia.fakeresponse.presentation.livedata.Success
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchViewModel(val workerDispatcher: CoroutineDispatcher,
                      val usecase: ChuckSearchUseCase,
                      val recordsUseCase: ShowRecordsUseCase) : ViewModel(), CoroutineScope {

    val searchLiveData = MutableLiveData<LiveDataResult<Pair<List<TransactionEntity>, List<ResponseListData>>>>()
    val exportLiveData = MutableLiveData<LiveDataResult<String>>()
    override val coroutineContext: CoroutineContext
        get() = workerDispatcher

    fun search(responseBody: String?, url: String?, requestBody: String?, tag: String?) {
        launch {
            try {
                val list = usecase.performSearch(requestBody = requestBody, responseBody = responseBody, url = url)
                val responseListData = recordsUseCase.search(url = url, tag = tag, response = responseBody)
                val pair = Pair(list, responseListData)
                searchLiveData.postValue(Success(pair))
            } catch (ex: Exception) {
                ex.printStackTrace()
                searchLiveData.postValue(Fail(ex))
            }
        }
    }

    fun export(sourceList: ArrayList<SearchType>) {
        launch {
            try {
                val gqlRecordList = arrayListOf<GqlRecord>()
                val restRecordList = arrayListOf<RestRecord>()
                val transactionList = arrayListOf<TransactionEntity>()

                val gqlRecordIds = arrayListOf<Int>()
                val restRecordIds = arrayListOf<Int>()
                sourceList.filter {
                    it.isSelectedForExport
                }.forEach {
                    if (it is ResponseListData) {
                        if (it.responseType == ResponseItemType.GQL) {
                            gqlRecordIds.add(it.id)
                        } else {
                            restRecordIds.add(it.id)
                        }

                    } else if (it is TransactionEntity) {
                        transactionList.add(it)
                    }
                }

                gqlRecordList.addAll(recordsUseCase.getGqlRecords(gqlRecordIds))
                restRecordList.addAll(recordsUseCase.getRestRecords(restRecordIds))

                val gson = Gson()
                val json = JsonObject()
                json.add(TRANSACTION, gson.toJsonTree(transactionList))
                json.add(GQL_RECORD, gson.toJsonTree(gqlRecordList))
                json.add(REST_RECORD, gson.toJsonTree(restRecordList))
                exportLiveData.postValue(Success(json.toString()))
            } catch (ex: Exception) {
                ex.printStackTrace()
                exportLiveData.postValue(Fail(ex))
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