package com.tokopedia.analyticsdebugger.cassava.validator.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.analyticsdebugger.cassava.data.CassavaSource
import com.tokopedia.analyticsdebugger.cassava.data.api.CassavaUrl
import com.tokopedia.analyticsdebugger.cassava.data.request.ValidationResultData
import com.tokopedia.analyticsdebugger.cassava.data.request.ValidationResultRequest
import com.tokopedia.analyticsdebugger.cassava.domain.JourneyListUseCase
import com.tokopedia.analyticsdebugger.cassava.domain.QueryListUseCase
import com.tokopedia.analyticsdebugger.cassava.domain.ValidationResultUseCase
import com.tokopedia.analyticsdebugger.cassava.validator.core.*
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.debugger.helper.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ValidatorViewModel @Inject constructor(private val context: Application,
                                             private val queryListUseCase: QueryListUseCase,
                                             private val validationResultUseCase: ValidationResultUseCase,
                                             private val journeyListUseCase: JourneyListUseCase)
    : AndroidViewModel(context) {

    private val dao: GtmLogDBSource by lazy { GtmLogDBSource(context) }
    private val engine: ValidatorEngine by lazy { ValidatorEngine(dao) }
    private val repo: GtmRepo by lazy { GtmRepo(TkpdAnalyticsDatabase.getInstance(context).gtmLogDao()) }

    private var journeyId: String = ""

    private val _testCases: MutableLiveData<List<Validator>> = MutableLiveData()
    val testCases: LiveData<List<Validator>>
        get() = _testCases

    private val _snackBarMessage = SingleLiveEvent<String>()
    val snackBarMessage: LiveData<String>
        get() = _snackBarMessage

    private val _listFiles = MutableLiveData<List<Pair<String, String>>>()
    val listFiles: LiveData<List<Pair<String, String>>>
        get() = _listFiles

    private val _cassavaQuery = MutableLiveData<CassavaQuery>()
    val cassavaQuery: LiveData<CassavaQuery>
        get() = _cassavaQuery

    private val _cassavaSource = MutableLiveData<CassavaSource>()
    val cassavaSource: LiveData<CassavaSource>
        get() = _cassavaSource

    init {
        changeSource(true)
    }

    fun changeSource(isFromNetwork: Boolean) {
        _cassavaSource.value = if (isFromNetwork) CassavaSource.NETWORK else CassavaSource.LOCAL
    }

    fun getCassavaSource(): CassavaSource =
        cassavaSource.value ?: CassavaSource.NETWORK

    fun run(queries: List<Pair<Int, Map<String, Any>>>, mode: String) {
        val v = queries.map { it.toDefaultValidator() }
        _testCases.value = v

        val startTime = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                val testResult = engine.computeCo(v, mode)
                _testCases.value = testResult
                val endTime = System.currentTimeMillis()
                if (cassavaSource.value == CassavaSource.NETWORK) {
                    sendValidationResult(testResult)
                }
                Timber.i("Retrieved in: ${endTime - startTime} Got ${testResult.size} results")
            } catch (e: Exception) {
                _snackBarMessage.setValue(e.message ?: "")
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            repo.delete()
            _snackBarMessage.setValue("Successfully deleted!")
        }
    }

    fun fetchJourneyQueriesList(isFromNetwork: Boolean) {
        viewModelScope.launch {
            try {
                _listFiles.postValue(journeyListUseCase.execute(isFromNetwork))
            } catch (e: Exception) {
                _snackBarMessage.postValue(e.message ?: "")
            }
        }
    }

    fun getListFiles(): List<Pair<String, String>> = listFiles.value ?: arrayListOf()

    fun fetchQueryFromAsset(filePath: String, isFromNetwork: Boolean) {
        changeSource(isFromNetwork)
        if (isFromNetwork) {
            this.journeyId = filePath
        }

        viewModelScope.launch(CoroutineDispatchersProvider.io) {
            try {
                _cassavaQuery.postValue(queryListUseCase.execute(
                        getCassavaSource(),
                        filePath))
            } catch (t: Throwable) {
                t.printStackTrace()
                _snackBarMessage.postValue(t.message)
            }
        }
    }

    private fun sendValidationResult(testResult: List<Validator>) {
        viewModelScope.launch(CoroutineDispatchersProvider.io) {
            validationResultUseCase.execute(
                    journeyId = journeyId,
                    validationResult = ValidationResultRequest(
                            version = "",
                            token = CassavaUrl.TOKEN,
                            data = testResult.map {
                                ValidationResultData(
                                        dataLayerId = it.id,
                                        result = it.status == Status.SUCCESS,
                                        errorMessage = ""
                                )
                            }.toList()
                    ))
        }
    }

}