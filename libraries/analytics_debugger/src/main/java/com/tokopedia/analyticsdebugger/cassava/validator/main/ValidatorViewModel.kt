package com.tokopedia.analyticsdebugger.cassava.validator.main

import androidx.lifecycle.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.analyticsdebugger.cassava.data.CassavaSource
import com.tokopedia.analyticsdebugger.cassava.domain.JourneyListUseCase
import com.tokopedia.analyticsdebugger.cassava.domain.QueryListUseCase
import com.tokopedia.analyticsdebugger.cassava.validator.core.*
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import com.tokopedia.utils.lifecycle.SingleLiveEvent
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

    private val _cassavaQuery = MutableLiveData<CassavaQuery>()
    val cassavaQuery: LiveData<CassavaQuery>
        get() = _cassavaQuery

    val testCases: LiveData<List<Validator>> = _cassavaQuery.switchMap {
        liveData(viewModelScope.coroutineContext) {
            val startTime = System.currentTimeMillis()
            val v = it.query.map { it.toDefaultValidator() }
            emit(v)
            runCatching {
                engine.computeCo(v, it.mode.value).also {
                    val endTime = System.currentTimeMillis()
                    Timber.i("Computed in: ${endTime - startTime} Got ${it.size} results")
                    emit(it)
                }
            }.onFailure { _snackBarMessage.setValue(it.message ?: "") }
        }
    }

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

    fun changeSource(isFromNetwork: Boolean) {
        _cassavaSource.value = if (isFromNetwork) CassavaSource.NETWORK else CassavaSource.LOCAL
    }

    fun getListFiles(): List<Pair<String, String>> = listFiles.value ?: arrayListOf()

    fun fetchQueryFromAsset(filePath: String, isFromNetwork: Boolean) {
        if (isFromNetwork) {
            this.journeyId = filePath
        }
        val source = if (isFromNetwork) CassavaSource.NETWORK else CassavaSource.LOCAL
        viewModelScope.launch {
            try {
                _cassavaQuery.value = queryListUseCase.execute(source, filePath)
            } catch (t: Throwable) {
                _snackBarMessage.setValue(t.message ?: "")
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            repo.delete()
            _snackBarMessage.setValue("Successfully deleted!")
        }
    }

}