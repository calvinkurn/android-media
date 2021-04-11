package com.tokopedia.cassavatest

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.GsonBuilder
import com.tokopedia.analyticsdebugger.cassava.data.CassavaRepository
import com.tokopedia.analyticsdebugger.cassava.data.CassavaSource
import com.tokopedia.analyticsdebugger.cassava.data.api.CassavaApi
import com.tokopedia.analyticsdebugger.cassava.domain.QueryListUseCase
import com.tokopedia.analyticsdebugger.cassava.validator.Utils
import com.tokopedia.analyticsdebugger.cassava.validator.core.*
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.schedulers.Schedulers

fun deleteCassavaDb(context: Context) =
        TkpdAnalyticsDatabase.getInstance(context).gtmLogDao().deleteAll()

fun getAnalyticsWithQuery(gtmLogDBSource: GtmLogDBSource,
                          context: Context,
                          journeyId: Int): List<Validator> {
    val testCases = getTestCases(context, journeyId)
    return ValidatorEngine(gtmLogDBSource)
            .computeRx(testCases.first, testCases.second)
            .toBlocking()
            .first()
}

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
    val queryPairs = getTestCases(InstrumentationRegistry.getInstrumentation().context, queryString)
    return ValidatorEngine(gtmLogDBSource)
            .computeRx(queryPairs.first, queryPairs.second)
            .toBlocking()
            .first()
}

internal fun getTestCases(context: Context, queryFileName: String): Pair<List<Validator>, String> {
    val cassavaQueryStr = Utils.getJsonDataFromAsset(context, queryFileName)
            ?: throw AssertionError("Cassava query is not found: \"$queryFileName\"")
    return queryFormat(cassavaQueryStr)
}

internal fun getTestCases(context: Context, journeyId: Int): Pair<List<Validator>, String> = runBlocking {
    val useCase = QueryListUseCase(CassavaRepository(getCassavaApi()))
    val query: CassavaQuery = useCase.execute(context, CassavaSource.NETWORK,
            journeyId, "") ?: throw Throwable("Failed to get Query")
    return@runBlocking query.query.map {
        it.toDefaultValidator()
    } to query.mode.value
}

private fun queryFormat(queryString: String): Pair<List<Validator>, String> {
    val q = queryString.toCassavaQuery()
    return q.query.map { it.toDefaultValidator() } to q.mode.value
}

private fun <T> Observable<T>.test(onNext: (T) -> Unit) {
    this.observeOn(Schedulers.immediate()).subscribeOn(Schedulers.immediate()).subscribe(onNext)
}

private fun getCassavaApi(): CassavaApi {
    val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create()))
//                    .baseUrl(TokopediaUrl.getInstance().API)
            .baseUrl("https://api-staging.tokopedia.com/")
            .build()

    return retrofit.create(CassavaApi::class.java)
}