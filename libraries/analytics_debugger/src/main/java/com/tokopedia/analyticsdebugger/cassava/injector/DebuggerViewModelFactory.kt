package com.tokopedia.analyticsdebugger.cassava.injector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.analyticsdebugger.cassava.debugger.DebuggerListViewModel
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDao

class DebuggerViewModelFactory(val dao: GtmLogDao) : ViewModelProvider.Factory {

    private val gtmRepo = GtmRepo(dao)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            when (modelClass) {
                DebuggerListViewModel::class.java -> DebuggerListViewModel(gtmRepo) as T
                else -> throw IllegalArgumentException("View Model class not found")
            }

}