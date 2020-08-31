package com.tokopedia.analyticsdebugger.debugger.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import com.tokopedia.analyticsdebugger.debugger.helper.formatDataExcerpt
import com.tokopedia.analyticsdebugger.debugger.ui.model.TopAdsDebuggerViewModel
import rx.Observable
import rx.functions.Func1
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TopAdsLogMapper @Inject
internal constructor() : Func1<TopAdsLogDB, Observable<Visitable<*>>> {
    private val dateFormat: SimpleDateFormat

    init {
        dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    }

    override fun call(topAdsLogDB: TopAdsLogDB): Observable<Visitable<*>> {
        val viewModel = TopAdsDebuggerViewModel()
        viewModel.id = topAdsLogDB.id
        viewModel.url = topAdsLogDB.url
        viewModel.previewUrl = formatDataExcerpt(topAdsLogDB.url)
        viewModel.eventType = topAdsLogDB.eventType
        viewModel.sourceName = topAdsLogDB.sourceName
        viewModel.productId = topAdsLogDB.productId
        viewModel.productName = topAdsLogDB.productName
        viewModel.imageUrl = topAdsLogDB.imageUrl
        viewModel.eventStatus = topAdsLogDB.eventStatus
        viewModel.timestamp = dateFormat.format(Date(topAdsLogDB.timestamp))
        viewModel.fullResponse = topAdsLogDB.fullResponse

        return Observable.just(viewModel as Visitable<*>)
    }
}
