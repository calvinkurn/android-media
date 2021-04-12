package com.tokopedia.cassavatest

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.validator.Utils
import com.tokopedia.analyticsdebugger.cassava.validator.core.*
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import rx.Observable
import rx.schedulers.Schedulers

fun deleteCassavaDb(context: Context) =
        TkpdAnalyticsDatabase.getInstance(context).gtmLogDao().deleteAll()


@Deprecated("consider using Cassava Test Rule")
fun getAnalyticsWithQuery(gtmLogDBSource: GtmLogDBSource,
                          context: Context,
                          queryFileName: String): List<Validator> {
    val cassavaQuery = getQuery(context, queryFileName)
    val validators = cassavaQuery.query.map { it.toDefaultValidator() }
    return ValidatorEngine(gtmLogDBSource)
            .computeRx(validators, cassavaQuery.mode.value)
            .toBlocking()
            .first()
}

@Deprecated("consider using Cassava Test Rule")
fun getAnalyticsWithQuery(gtmLogDBSource: GtmLogDBSource, queryString: String): List<Validator> {
    val cassavaQuery = getQuery(InstrumentationRegistry.getInstrumentation().context, queryString)
    val validators = cassavaQuery.query.map { it.toDefaultValidator() }
    return ValidatorEngine(gtmLogDBSource)
            .computeRx(validators, cassavaQuery.mode.value)
            .toBlocking()
            .first()
}

internal fun getQuery(context: Context, queryFileName: String): CassavaQuery {
    val queryJson = Utils.getJsonDataFromAsset(context, queryFileName)
            ?: throw AssertionError("Cassava query is not found: \"$queryFileName\"")
    return queryJson.toCassavaQuery()
}

private fun <T> Observable<T>.test(onNext: (T) -> Unit) {
    this.observeOn(Schedulers.immediate()).subscribeOn(Schedulers.immediate()).subscribe(onNext)
}