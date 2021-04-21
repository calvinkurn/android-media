package com.tokopedia.cassavatest

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.GsonBuilder
import com.tokopedia.analyticsdebugger.cassava.data.CassavaRepository
import com.tokopedia.analyticsdebugger.cassava.data.CassavaSource
import com.tokopedia.analyticsdebugger.cassava.data.api.CassavaApi
import com.tokopedia.analyticsdebugger.cassava.data.api.CassavaUrl
import com.tokopedia.analyticsdebugger.cassava.data.request.ValidationResultData
import com.tokopedia.analyticsdebugger.cassava.data.request.ValidationResultRequest
import com.tokopedia.analyticsdebugger.cassava.domain.QueryListUseCase
import com.tokopedia.analyticsdebugger.cassava.domain.ValidationResultUseCase
import com.tokopedia.analyticsdebugger.cassava.validator.Utils
import com.tokopedia.analyticsdebugger.cassava.validator.core.*
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.url.TokopediaUrl
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.schedulers.Schedulers

fun deleteCassavaDb(context: Context) =
        TkpdAnalyticsDatabase.getInstance(context).gtmLogDao().deleteAll()

@Deprecated("Consider using Cassava Test Rule")
fun getAnalyticsWithQuery(gtmLogDBSource: GtmLogDBSource,
                          queryId: String,
                          isFromNetwork: Boolean): List<Validator> {
    val cassavaQuery = getQuery(InstrumentationRegistry.getInstrumentation().context, queryId, isFromNetwork)
    val validators = cassavaQuery.query.map { it.toDefaultValidator() }
    val validationResult = ValidatorEngine(gtmLogDBSource)
            .computeRx(validators, cassavaQuery.mode.value)
            .toBlocking()
            .first()
    if (isFromNetwork) sendTestResult(queryId, validationResult)
    return validationResult
}

@Deprecated("Consider using Cassava Test Rule")
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

@Deprecated("Consider using Cassava Test Rule")
fun getAnalyticsWithQuery(gtmLogDBSource: GtmLogDBSource, queryString: String): List<Validator> {
    val cassavaQuery = getQuery(InstrumentationRegistry.getInstrumentation().context, queryString)
    val validators = cassavaQuery.query.map { it.toDefaultValidator() }
    return ValidatorEngine(gtmLogDBSource)
            .computeRx(validators, cassavaQuery.mode.value)
            .toBlocking()
            .first()
}

internal fun getQuery(context: Context, queryFileName: String): CassavaQuery =
        Utils.getJsonDataFromAsset(context, queryFileName)?.toCassavaQuery()
                ?: throw AssertionError("Cassava query is not found: \"$queryFileName\"")

internal fun getQuery(context: Context, queryId: String, isFromNetwork: Boolean): CassavaQuery =
        runBlocking {
            val useCase = QueryListUseCase(context, CassavaRepository(getCassavaApi()))
            return@runBlocking useCase.execute(
                    if (isFromNetwork) CassavaSource.NETWORK else CassavaSource.LOCAL,
                    queryId
            ) ?: throw Throwable("Failed to get Query")
        }

internal fun sendTestResult(journeyId: String, testResult: List<Validator>) {
    runBlocking {
        val useCase = ValidationResultUseCase(CassavaRepository(getCassavaApi()))
        useCase.execute(
                journeyId,
                ValidationResultRequest(
                        version = "",
                        token = CassavaUrl.TOKEN,
                        data = testResult.map {
                            ValidationResultData(
                                    dataLayerId = it.id,
                                    result = it.status == Status.SUCCESS,
                                    errorMessage = ""
                            )
                        }.toList()
                )
        )
    }
}

private fun <T> Observable<T>.test(onNext: (T) -> Unit) {
    this.observeOn(Schedulers.immediate()).subscribeOn(Schedulers.immediate()).subscribe(onNext)
}

private fun getCassavaApi(): CassavaApi {
    val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create()))
            .client(okHttpClient)
            .baseUrl(TokopediaUrl.getInstance().API)
            .build()

    return retrofit.create(CassavaApi::class.java)
}