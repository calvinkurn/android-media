package com.tokopedia.journeydebugger.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable

import java.text.SimpleDateFormat
import java.util.Date

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

import com.tokopedia.journeydebugger.database.JourneyLogDB
import com.tokopedia.journeydebugger.helper.formatDataExcerpt
import com.tokopedia.journeydebugger.ui.model.JourneyDebuggerUIModel

class JourneyLogMapper @Inject
internal constructor() : Func1<JourneyLogDB, Observable<Visitable<*>>> {
    private val dateFormat: SimpleDateFormat

    init {
        dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    }

    override fun call(applinkLogDB: JourneyLogDB): Observable<Visitable<*>> {
        val uiModel = JourneyDebuggerUIModel()
        uiModel.id = applinkLogDB.id
        uiModel.journey = applinkLogDB.journey
        uiModel.previewJourney = formatDataExcerpt(applinkLogDB.journey)
        uiModel.timestamp = dateFormat.format(Date(applinkLogDB.timestamp))

        return Observable.just(uiModel as Visitable<*>)
    }
}
