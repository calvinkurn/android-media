package com.tokopedia.analyticsdebugger.debugger.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.database.FpmLogDB
import com.tokopedia.analyticsdebugger.debugger.ui.model.FpmDebuggerViewModel

import java.text.SimpleDateFormat
import java.util.Date

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

import com.tokopedia.analyticsdebugger.debugger.helper.formatDataExcerpt

/**
 * @author okasurya on 5/16/18.
 */
class FpmLogMapper @Inject
internal constructor() : Func1<FpmLogDB, Observable<Visitable<*>>> {
    private val dateFormat: SimpleDateFormat

    init {
        dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    }

    override fun call(fpmLogDB: FpmLogDB): Observable<Visitable<*>> {
        val viewModel = FpmDebuggerViewModel()
        viewModel.id = fpmLogDB.id
        viewModel.name = fpmLogDB.traceName
        viewModel.duration = fpmLogDB.duration
        viewModel.metrics = fpmLogDB.metrics
        viewModel.attributes = fpmLogDB.attributes
        viewModel.previewMetrics = formatDataExcerpt(fpmLogDB.metrics)
        viewModel.previewAttributes = formatDataExcerpt(fpmLogDB.attributes)
        viewModel.timestamp = dateFormat.format(Date(fpmLogDB.timestamp))

        return Observable.just(viewModel as Visitable<*>)
    }
}
