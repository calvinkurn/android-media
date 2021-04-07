package com.tokopedia.analyticsdebugger.cassava.validator.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.analyticsdebugger.cassava.validator.Utils
import com.tokopedia.analyticsdebugger.cassava.validator.core.*
import com.tokopedia.analyticsdebugger.cassava.validator.list.ValidatorListFragment
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.debugger.helper.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ValidatorViewModel @Inject constructor(val context: Application)
    : AndroidViewModel(context) {

    private val dao: GtmLogDBSource by lazy { GtmLogDBSource(context) }
    private val engine: ValidatorEngine by lazy { ValidatorEngine(dao) }
    private val repo: GtmRepo by lazy { GtmRepo(TkpdAnalyticsDatabase.getInstance(context).gtmLogDao()) }

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

    fun run(queries: List<Map<String, Any>>, mode: String) {
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

    fun fetchLocalQueriesList() {
        viewModelScope.launch {
            try {
                _listFiles.postValue(Utils.listAssetFiles(context, ValidatorListFragment.TRACKER_ROOT_PATH))
            } catch (e: Exception) {
                _snackBarMessage.postValue(e.message ?: "")
            }
        }
    }

    fun getListFiles(): List<String> = listFiles.value ?: arrayListOf()

    fun fetchQueryFromAsset(filePath: String) {
        viewModelScope.launch {
            try {
                _cassavaQuery.postValue(Utils.getJsonDataFromAsset(context, filePath)?.toCassavaQuery())
            } catch (e: Exception) {
                e.printStackTrace()
                _snackBarMessage.postValue(e.message)
            }
        }
    }

}