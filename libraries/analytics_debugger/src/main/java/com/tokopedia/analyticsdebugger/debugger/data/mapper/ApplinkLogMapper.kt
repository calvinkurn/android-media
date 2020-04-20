package com.tokopedia.analyticsdebugger.debugger.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.database.ApplinkLogDB
import com.tokopedia.analyticsdebugger.debugger.ui.model.ApplinkDebuggerViewModel

import java.text.SimpleDateFormat
import java.util.Date

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

import com.tokopedia.analyticsdebugger.debugger.helper.formatDataExcerpt

class ApplinkLogMapper @Inject
internal constructor() : Func1<ApplinkLogDB, Observable<Visitable<*>>> {
    private val dateFormat: SimpleDateFormat

    init {
        dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    }

    override fun call(applinkLogDB: ApplinkLogDB): Observable<Visitable<*>> {
        val viewModel = ApplinkDebuggerViewModel()
        viewModel.id = applinkLogDB.id
        viewModel.applink = applinkLogDB.applink
        viewModel.trace = applinkLogDB.traces
        viewModel.previewTrace = formatDataExcerpt(applinkLogDB.traces)
        viewModel.timestamp = dateFormat.format(Date(applinkLogDB.timestamp))

        return Observable.just(viewModel as Visitable<*>)
    }
}
