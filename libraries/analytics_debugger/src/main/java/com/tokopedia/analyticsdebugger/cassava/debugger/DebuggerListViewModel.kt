package com.tokopedia.analyticsdebugger.cassava.debugger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import kotlinx.coroutines.launch

class DebuggerListViewModel(private val repo: GtmRepo) : ViewModel() {

    val logData: MutableLiveData<List<GtmLogDB>> = MutableLiveData()

    fun fetchLogs() {
        viewModelScope.launch {
            val results = repo.getLogs()
            logData.value = results
        }
    }
}