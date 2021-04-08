package com.tokopedia.analyticsdebugger.cassava.validator.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.cassava.data.CassavaSource
import com.tokopedia.analyticsdebugger.cassava.domain.QueryListUseCase
import com.tokopedia.analyticsdebugger.cassava.validator.Utils
import com.tokopedia.analyticsdebugger.cassava.validator.core.CassavaQuery
import com.tokopedia.analyticsdebugger.cassava.validator.core.Validator
import com.tokopedia.analyticsdebugger.cassava.validator.core.ValidatorEngine
import com.tokopedia.analyticsdebugger.cassava.validator.core.toDefaultValidator
import com.tokopedia.analyticsdebugger.cassava.validator.list.ValidatorListFragment
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.debugger.helper.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ValidatorViewModel @Inject constructor(private val context: Application,
                                             private val queryListUseCase: QueryListUseCase)
    : AndroidViewModel(context) {

    private val dao: GtmLogDBSource by lazy { GtmLogDBSource(context) }
    private val engine: ValidatorEngine by lazy { ValidatorEngine(dao) }
    private val repo: GtmRepo by lazy { GtmRepo(TkpdAnalyticsDatabase.getInstance(context).gtmLogDao()) }

    private val localCacheHandler: LocalCacheHandler by lazy { LocalCacheHandler(context, PREF_NAME) }

    private val _testCases: MutableLiveData<List<Validator>> = MutableLiveData()
    val testCases: LiveData<List<Validator>>
        get() = _testCases

    private val _snackBarMessage = SingleLiveEvent<String>()
    val snackBarMessage: LiveData<String>
        get() = _snackBarMessage

    private val _listFiles = MutableLiveData<List<String>>()
    val listFiles: LiveData<List<String>>
        get() = _listFiles

    private val _cassavaQuery = MutableLiveData<CassavaQuery>()
    val cassavaQuery: LiveData<CassavaQuery>
        get() = _cassavaQuery

    private val _cassavaSource = MutableLiveData<CassavaSource>()
    val cassavaSource: LiveData<CassavaSource>
        get() = _cassavaSource

    init {
        setCassavaSource(localCacheHandler.getBoolean(KEY_IS_NETWORK, true))
    }

    private fun setCassavaSource(isFromNetwork: Boolean) {
        _cassavaSource.value = if (isFromNetwork) CassavaSource.NETWORK else CassavaSource.LOCAL
    }

    fun changeSource(isFromNetwork: Boolean) {
        setCassavaSource(isFromNetwork)
        localCacheHandler.apply {
            putBoolean(KEY_IS_NETWORK, isFromNetwork)
            applyEditor()
        }
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

    fun fetchJourneyQueriesList() {
        viewModelScope.launch {
            try {
                _listFiles.postValue(Utils.listAssetFiles(context, ValidatorListFragment.TRACKER_ROOT_PATH))
            } catch (e: Exception) {
                _snackBarMessage.postValue(e.message ?: "")
            }
        }
    }

    fun getListFiles(): List<String> = listFiles.value ?: arrayListOf()

    fun fetchQueryFromAsset(filePath: String, journeyId: Int) {
        viewModelScope.launch(CoroutineDispatchersProvider.io) {
            try {
                _cassavaQuery.postValue(queryListUseCase.execute(
                        context,
                        cassavaSource.value ?: CassavaSource.NETWORK,
                        journeyId,
                        filePath))
            } catch (t: Throwable) {
                t.printStackTrace()
                _snackBarMessage.postValue(t.message)
            }
        }
    }

    companion object {
        private const val PREF_NAME = "pref_cassava"

        private const val KEY_IS_NETWORK = "is_network"
    }

}