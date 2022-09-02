package com.tokopedia.cassavatest

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.GsonBuilder
import com.tokopedia.analyticsdebugger.cassava.core.CassavaQuery
import com.tokopedia.analyticsdebugger.cassava.core.Status
import com.tokopedia.analyticsdebugger.cassava.core.Validator
import com.tokopedia.analyticsdebugger.cassava.data.CassavaRepository
import com.tokopedia.analyticsdebugger.cassava.data.CassavaSource
import com.tokopedia.analyticsdebugger.cassava.data.api.CassavaApi
import com.tokopedia.analyticsdebugger.cassava.data.request.ValidationResultData
import com.tokopedia.analyticsdebugger.cassava.data.request.ValidationResultRequest
import com.tokopedia.analyticsdebugger.cassava.domain.QueryListUseCase
import com.tokopedia.analyticsdebugger.cassava.domain.ValidationResultUseCase
import com.tokopedia.url.TokopediaUrl
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

private fun getCassavaApi(): CassavaApi {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    val retrofit = Retrofit.Builder()
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create()
            )
        )
        .client(okHttpClient)
        .baseUrl(TokopediaUrl.getInstance().API)
        .build()

    return retrofit.create(CassavaApi::class.java)
}