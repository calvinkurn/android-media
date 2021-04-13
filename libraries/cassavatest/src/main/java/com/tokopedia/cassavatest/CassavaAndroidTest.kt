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
import com.tokopedia.analyticsdebugger.cassava.validator.core.*
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.schedulers.Schedulers

fun deleteCassavaDb(context: Context) =
        TkpdAnalyticsDatabase.getInstance(context).gtmLogDao().deleteAll()

fun getAnalyticsWithQuery(gtmLogDBSource: GtmLogDBSource,
                          context: Context,
                          journeyId: Int): List<Validator> {
    val cassavaQuery = getQuery(context, journeyId)
    val validators = cassavaQuery.query.map { it.toDefaultValidator() }
    val validationResult = ValidatorEngine(gtmLogDBSource)
            .computeRx(validators, cassavaQuery.mode.value)
            .toBlocking()
            .first()
    sendTestResult(journeyId, validationResult)
    return validationResult
}

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

fun getAnalyticsWithQuery(gtmLogDBSource: GtmLogDBSource, queryString: String): List<Validator> {
    val cassavaQuery = getQuery(InstrumentationRegistry.getInstrumentation().context, queryString)
    val validators = cassavaQuery.query.map { it.toDefaultValidator() }
    return ValidatorEngine(gtmLogDBSource)
            .computeRx(validators, cassavaQuery.mode.value)
            .toBlocking()
            .first()
}

internal fun getQuery(context: Context, queryFileName: String): CassavaQuery = runBlocking {
    val useCase = QueryListUseCase(CassavaRepository(getCassavaApi()))
    return@runBlocking useCase.execute(context, CassavaSource.LOCAL, 0, queryFileName)
            ?: throw AssertionError("Cassava query is not found: \"$queryFileName\"")
}

internal fun getQuery(context: Context, journeyId: Int): CassavaQuery = runBlocking {
    val useCase = QueryListUseCase(CassavaRepository(getCassavaApi()))
    return@runBlocking useCase.execute(context, CassavaSource.NETWORK,
            journeyId, "") ?: throw Throwable("Failed to get Query")
}

internal fun sendTestResult(journeyId: Int, testResult: List<Validator>) {
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
//                    .baseUrl(TokopediaUrl.getInstance().API)
            .baseUrl("https://api-staging.tokopedia.com/")
            .build()

    return retrofit.create(CassavaApi::class.java)
}