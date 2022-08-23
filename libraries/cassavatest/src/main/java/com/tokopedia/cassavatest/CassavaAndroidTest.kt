package com.tokopedia.cassavatest

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.GsonBuilder
import com.tokopedia.analyticsdebugger.cassava.utils.AnalyticsParser
import com.tokopedia.analyticsdebugger.cassava.data.CassavaRepository
import com.tokopedia.analyticsdebugger.cassava.data.CassavaSource
import com.tokopedia.analyticsdebugger.cassava.data.api.CassavaApi
import com.tokopedia.analyticsdebugger.cassava.data.request.ValidationResultData
import com.tokopedia.analyticsdebugger.cassava.data.request.ValidationResultRequest
import com.tokopedia.analyticsdebugger.cassava.domain.QueryListUseCase
import com.tokopedia.analyticsdebugger.cassava.domain.ValidationResultUseCase
import com.tokopedia.analyticsdebugger.cassava.utils.Utils
import com.tokopedia.analyticsdebugger.cassava.core.*
import com.tokopedia.analyticsdebugger.database.CassavaDatabase
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
        CassavaDatabase.getInstance(context).cassavaDao().deleteAll()

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
                        token = InstrumentationRegistry.getInstrumentation().context
                                .getString(com.tokopedia.keys.R.string.thanos_token_key),
                        data = testResult.map {
                            val cassavaResult: Boolean = it.status == Status.SUCCESS

                            ValidationResultData(
                                    dataLayerId = it.id,
                                    result = cassavaResult,
                                    errorMessage = if (!cassavaResult) it.errors else ""
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