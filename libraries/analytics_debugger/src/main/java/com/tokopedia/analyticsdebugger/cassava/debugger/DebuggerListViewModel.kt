package com.tokopedia.analyticsdebugger.cassava.debugger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import kotlinx.coroutines.launch

class DebuggerListViewModel(private val repo: GtmRepo) : ViewModel() {

    val logData: MutableLiveData<List<GtmLogDB>> = MutableLiveData()

    fun searchLogs(query: String) {
        viewModelScope.launch {
            logData.value = repo.search(query)
        }
    }

    fun listScrolled() {
        viewModelScope.launch {
            logData.value = repo.requestMore()
        }
    }

    fun delete() {
        viewModelScope.launch {
            repo.delete()
        }
    }


}