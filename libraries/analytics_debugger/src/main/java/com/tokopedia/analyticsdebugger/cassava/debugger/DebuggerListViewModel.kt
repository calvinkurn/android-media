package com.tokopedia.analyticsdebugger.cassava.debugger

import androidx.lifecycle.*
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import kotlinx.coroutines.launch

class DebuggerListViewModel(private val repo: GtmRepo) : ViewModel() {

    /*
    * when the value is empty ("") it refreshes with all logs
    * when the value is string it searches the log with the query
    * when the value is null it loadsMore with previous query (saved in [GtmRepo] class)
    * */
    private val queryTrigger = MutableLiveData<String?>()
    val logData: LiveData<List<GtmLogDB>> = Transformations.switchMap(queryTrigger) {
        liveData(viewModelScope.coroutineContext) {
            emit(if (it != null) repo.search(it) else repo.requestMore())
        }
    }

    init {
        queryTrigger.value = ""
    }

    fun searchLogs(query: String) {
        queryTrigger.value = query
    }

    fun listScrolled() {
        queryTrigger.value = null
    }

    fun delete() {
        viewModelScope.launch {
            repo.delete()
        }
    }


}