package com.tokopedia.cassavatest

import android.content.Context
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.cassava.validator.Utils
import com.tokopedia.analyticsdebugger.cassava.validator.core.Validator
import com.tokopedia.analyticsdebugger.cassava.validator.core.ValidatorEngine
import com.tokopedia.analyticsdebugger.cassava.validator.core.toCassavaQuery
import com.tokopedia.analyticsdebugger.cassava.validator.core.toDefaultValidator
import rx.Observable
import rx.schedulers.Schedulers

fun deleteCassavaDb(context: Context) =
        TkpdAnalyticsDatabase.getInstance(context).gtmLogDao().deleteAll()


fun getAnalyticsWithQuery(gtmLogDBSource: GtmLogDBSource,
                          context: Context,
                          queryFileName: String): List<Validator> {
    val testCases = getTestCases(context, queryFileName)
    return ValidatorEngine(gtmLogDBSource)
            .computeRx(testCases.first, testCases.second)
            .toBlocking()
            .first()
}

fun getAnalyticsWithQuery(gtmLogDBSource: GtmLogDBSource, queryString: String): List<Validator> {
    val queryPairs = queryFormat(queryString)
    return ValidatorEngine(gtmLogDBSource)
            .computeRx(queryPairs.first, queryPairs.second)
            .toBlocking()
            .first()
}

private fun getTestCases(context: Context, queryFileName: String): Pair<List<Validator>, String> {
    val cassavaQueryStr = Utils.getJsonDataFromAsset(context, queryFileName)
            ?: throw AssertionError("Cassava query is not found: \"$queryFileName\"")
    return queryFormat(cassavaQueryStr)
}

private fun queryFormat(queryString: String): Pair<List<Validator>, String> {
    val q = queryString.toCassavaQuery()
    return q.query.map { it.toDefaultValidator() } to q.mode.value
}

private fun <T> Observable<T>.test(onNext: (T) -> Unit) {
    this.observeOn(Schedulers.immediate()).subscribeOn(Schedulers.immediate()).subscribe(onNext)
}