package com.tokopedia.settingbank.choosebank.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.network.NetworkRouter
import com.tokopedia.settingbank.choosebank.data.BankListApi
import com.tokopedia.settingbank.choosebank.data.BankListUrl
import com.tokopedia.settingbank.choosebank.domain.mapper.GetBankListDBMapper
import com.tokopedia.settingbank.choosebank.domain.mapper.GetBankListWSMapper
import com.tokopedia.settingbank.choosebank.domain.usecase.GetBankListDBUseCase
import com.tokopedia.settingbank.choosebank.domain.usecase.GetBankListWSUseCase
import com.tokopedia.settingbank.choosebank.view.presenter.ChooseBankPresenter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author by nisie on 7/2/18.
 */
class ChooseBankDependencyInjector {

    object Companion {

        fun inject(context: Context): ChooseBankPresenter {

            val session : UserSessionInterface = UserSession(context)

            val gson: Gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .setPrettyPrinting()
                    .serializeNulls().create()

            val stringResponseConverter = com.tokopedia.network.converter.StringResponseConverter()

            val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
                    .addConverterFactory(stringResponseConverter)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

            val chuckInterceptor = ChuckInterceptor(context)

            val httpLoggingInterceptor = HttpLoggingInterceptor()

            if (GlobalConfig.isAllowDebuggingTools()) {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
            }

            val networkRouter: NetworkRouter = context as NetworkRouter

            val fingerprintInterceptor = com.tokopedia.network.interceptor.FingerprintInterceptor(networkRouter, session)

            val tkpdAuthInterceptor = com.tokopedia.network.interceptor.TkpdAuthInterceptor(context,
                    networkRouter, session)

            val builder: OkHttpClient.Builder = OkHttpClient.Builder()

            if (GlobalConfig.isAllowDebuggingTools()) {
                builder.addInterceptor(chuckInterceptor)
                builder.addInterceptor(DebugInterceptor())
                builder.addInterceptor(httpLoggingInterceptor)
            }

            val headerResponseInterceptor =
                    HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java)

            builder.addInterceptor(fingerprintInterceptor)
            builder.addInterceptor(tkpdAuthInterceptor)
            builder.addInterceptor(headerResponseInterceptor)


            val okHttpClient: OkHttpClient = builder.build()

            val retrofit: Retrofit = retrofitBuilder.baseUrl(BankListUrl.BASE_URL)
                    .client(okHttpClient)
                    .build()

            val bankListApi: BankListApi = retrofit.create(BankListApi::class.java)

            val getBankListDBMapper = GetBankListDBMapper()
            val getBankListDBUseCase = GetBankListDBUseCase(getBankListDBMapper)

            val getBankListWSMapper = GetBankListWSMapper()
            val getBankListWSUseCase = GetBankListWSUseCase(bankListApi, getBankListWSMapper)

            //Originally from HomeRouter
            val bankCache = LocalCacheHandler(context, "FETCH_BANK")

            return ChooseBankPresenter(session, getBankListDBUseCase, getBankListWSUseCase, bankCache)
        }
    }
}