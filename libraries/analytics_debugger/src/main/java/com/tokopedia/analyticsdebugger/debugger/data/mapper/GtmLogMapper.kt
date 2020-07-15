package com.tokopedia.analyticsdebugger.debugger.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.ui.model.AnalyticsDebuggerViewModel

import java.text.SimpleDateFormat
import java.util.Date

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

import com.tokopedia.analyticsdebugger.debugger.helper.formatDataExcerpt

/**
 * @author okasurya on 5/16/18.
 */
class GtmLogMapper @Inject
internal constructor() : Func1<GtmLogDB, Observable<Visitable<*>>> {
    private val dateFormat: SimpleDateFormat

    init {
        dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    }

    override fun call(gtmLogDB: GtmLogDB): Observable<Visitable<*>> {
        val viewModel = AnalyticsDebuggerViewModel()
        viewModel.id = gtmLogDB.id
        viewModel.name = gtmLogDB.name
        viewModel.category = gtmLogDB.category
        viewModel.data = gtmLogDB.data
        viewModel.dataExcerpt = formatDataExcerpt(gtmLogDB.data)
        viewModel.timestamp = dateFormat.format(Date(gtmLogDB.timestamp))

        return Observable.just(viewModel as Visitable<*>)
    }
}
