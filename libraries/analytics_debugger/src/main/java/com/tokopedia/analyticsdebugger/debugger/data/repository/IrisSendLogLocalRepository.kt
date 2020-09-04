package com.tokopedia.analyticsdebugger.debugger.data.repository

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.database.IrisSendLogDB
import com.tokopedia.analyticsdebugger.debugger.data.source.IrisSendLogDBSource
import com.tokopedia.analyticsdebugger.debugger.ui.model.AnalyticsDebuggerViewModel
import com.tokopedia.usecase.RequestParams

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

import com.tokopedia.analyticsdebugger.debugger.helper.formatDataExcerpt

class IrisSendLogLocalRepository @Inject
internal constructor(private val dbSource: IrisSendLogDBSource) {
    private val dateFormat: DateFormat

    init {
        dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    }

    fun removeAll(): Observable<Boolean> {
        return dbSource.deleteAll()
    }

    operator fun get(parameters: RequestParams): Observable<List<Visitable<*>>> {
        return dbSource.getData(parameters.parameters)
                .flatMapIterable { logDB -> logDB }.flatMap<Visitable<*>> { logDB ->
                    val viewModel = AnalyticsDebuggerViewModel()
                    viewModel.id = logDB.timestamp
                    viewModel.name = ""
                    viewModel.category = ""
                    viewModel.data = logDB.data
                    viewModel.dataExcerpt = formatDataExcerpt(logDB.data)
                    viewModel.timestamp = dateFormat.format(Date(logDB.timestamp))

                    Observable.just(viewModel)
                }.toList()
    }

    fun getCount(): Int {
        return dbSource.getCount()
    }
}
