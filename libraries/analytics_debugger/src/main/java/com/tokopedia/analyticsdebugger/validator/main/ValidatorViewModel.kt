package com.tokopedia.analyticsdebugger.validator.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.Validator
import com.tokopedia.analyticsdebugger.validator.core.ValidatorEngine
import com.tokopedia.analyticsdebugger.validator.core.toDefaultValidator
import kotlinx.coroutines.launch
import timber.log.Timber

class ValidatorViewModel constructor(val context: Application) : AndroidViewModel(context) {

    private val dao: GtmLogDBSource by lazy { GtmLogDBSource(context) }
    private val engine: ValidatorEngine by lazy { ValidatorEngine(dao) }

    private val _testCases: MutableLiveData<List<Validator>> by lazy {
        MutableLiveData<List<Validator>>()
    }

    val testCases: LiveData<List<Validator>>
        get() = _testCases

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
                Timber.w(e)
            }
        }
    }

}