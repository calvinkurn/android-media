package com.tokopedia.settingbank.banklist.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.settingbank.banklist.data.SettingBankApi
import com.tokopedia.settingbank.banklist.data.SettingBankUrl
import com.tokopedia.settingbank.banklist.domain.mapper.DeleteBankAccountMapper
import com.tokopedia.settingbank.banklist.domain.mapper.GetBankAccountListMapper
import com.tokopedia.settingbank.banklist.domain.mapper.SetDefaultBankAccountMapper
import com.tokopedia.settingbank.banklist.domain.usecase.DeleteBankAccountUseCase
import com.tokopedia.settingbank.banklist.domain.usecase.GetBankAccountListUseCase
import com.tokopedia.settingbank.banklist.domain.usecase.SetDefaultBankAccountUseCase
import com.tokopedia.settingbank.banklist.view.presenter.SettingBankPresenter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author by nisie on 6/8/18.
 */
class SettingBankDependencyInjector {

    object Companion {

        fun inject(context: Context): SettingBankPresenter {

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

            val networkRouter : NetworkRouter = context as NetworkRouter

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

            val retrofit: Retrofit = retrofitBuilder.baseUrl(SettingBankUrl.BASE_URL)
                    .client(okHttpClient)
                    .build()

            val settingBankApi: SettingBankApi = retrofit.create(SettingBankApi::class.java)

            val getBankListMapper = GetBankAccountListMapper()

            val getBankListUseCase = GetBankAccountListUseCase(settingBankApi, getBankListMapper)

            val setDefaultBankAccountMapper = SetDefaultBankAccountMapper()

            val setDefaultBankAccountUseCase = SetDefaultBankAccountUseCase(settingBankApi,
                    setDefaultBankAccountMapper)

            val deleteBankAccountMapper = DeleteBankAccountMapper()

            val deleteBankAccountUseCase = DeleteBankAccountUseCase(settingBankApi, deleteBankAccountMapper)

            return SettingBankPresenter(session, getBankListUseCase, setDefaultBankAccountUseCase, deleteBankAccountUseCase)
        }
    }
}