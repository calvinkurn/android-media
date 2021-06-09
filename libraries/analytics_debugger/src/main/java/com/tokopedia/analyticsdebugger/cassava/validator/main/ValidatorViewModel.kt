package com.tokopedia.analyticsdebugger.cassava.validator.main

import androidx.lifecycle.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.analyticsdebugger.cassava.data.CassavaSource
import com.tokopedia.analyticsdebugger.cassava.domain.JourneyListUseCase
import com.tokopedia.analyticsdebugger.cassava.domain.QueryListUseCase
import com.tokopedia.analyticsdebugger.cassava.validator.core.CassavaQuery
import com.tokopedia.analyticsdebugger.cassava.validator.core.Validator
import com.tokopedia.analyticsdebugger.cassava.validator.core.ValidatorEngine
import com.tokopedia.analyticsdebugger.cassava.validator.core.toDefaultValidator
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import com.tokopedia.analyticsdebugger.debugger.helper.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ValidatorViewModel @Inject constructor(
        private val queryListUseCase: QueryListUseCase,
        private val journeyListUseCase: JourneyListUseCase,
        private val engine: ValidatorEngine,
        private val repo: GtmRepo
) : ViewModel() {

    private var journeyId: String = ""

    private val _testCases: MutableLiveData<List<Validator>> = MutableLiveData()
    val testCases: LiveData<List<Validator>>
        get() = _testCases

    private val _snackBarMessage = SingleLiveEvent<String>()
    val snackBarMessage: LiveData<String>
        get() = _snackBarMessage

    private val _cassavaSource = MutableLiveData<CassavaSource>()

    val listFiles = _cassavaSource.switchMap {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            runCatching { emit(journeyListUseCase.execute(it)) }
                    .onFailure { _snackBarMessage.postValue(it.message ?: "") }
        }
    }

    private val _cassavaQuery = MutableLiveData<CassavaQuery>()
    val cassavaQuery: LiveData<CassavaQuery>
        get() = _cassavaQuery

    fun changeSource(isFromNetwork: Boolean) {
        _cassavaSource.value = if (isFromNetwork) CassavaSource.NETWORK else CassavaSource.LOCAL
    }

    fun run(queries: List<Pair<Int, Map<String, Any>>>, mode: String) {
        val v = queries.map { it.toDefaultValidator() }
        _testCases.value = v

        val startTime = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                val testResult = engine.computeCo(v, mode)
                _testCases.value = testResult
                val endTime = System.currentTimeMillis()
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

    fun getListFiles(): List<Pair<String, String>> = listFiles.value ?: arrayListOf()

    fun fetchQueryFromAsset(filePath: String, isFromNetwork: Boolean) {
        if (isFromNetwork) {
            this.journeyId = filePath
        }
        val source = if (isFromNetwork) CassavaSource.NETWORK else CassavaSource.LOCAL
        viewModelScope.launch(CoroutineDispatchersProvider.io) {
            try {
                _cassavaQuery.postValue(queryListUseCase.execute(source, filePath))
            } catch (t: Throwable) {
                t.printStackTrace()
                _snackBarMessage.postValue(t.message)
            }
        }
    }

}