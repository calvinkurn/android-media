package com.tokopedia.analyticsdebugger.cassava.debugger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import kotlinx.coroutines.launch

class DebuggerListViewModel(private val repo: GtmRepo) : ViewModel() {

    val logData: MutableLiveData<List<GtmLogDB>> = MutableLiveData()
    var isLast = false

    fun searchLogs(query: String) {
        isLast = false
        viewModelScope.launch {
            val results = repo.search(query)
            logData.value = results

        }
    }

    fun listScrolled() {
        if (isLast) return // to avoid duplicate trigger on scroll listener
        viewModelScope.launch {
            val results = repo.requestMore()
            logData.value = results
        }
    }

    fun delete() {
        viewModelScope.launch {
            repo.delete()
        }
    }


}