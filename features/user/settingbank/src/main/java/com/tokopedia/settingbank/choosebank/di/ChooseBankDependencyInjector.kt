package com.tokopedia.settingbank.choosebank.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor
import com.tokopedia.core.router.home.HomeRouter
import com.tokopedia.settingbank.banklist.data.SettingBankApi
import com.tokopedia.settingbank.banklist.data.SettingBankUrl
import com.tokopedia.settingbank.choosebank.data.BankListApi
import com.tokopedia.settingbank.choosebank.data.BankListUrl
import com.tokopedia.settingbank.choosebank.domain.mapper.GetBankListDBMapper
import com.tokopedia.settingbank.choosebank.domain.mapper.GetBankListWSMapper
import com.tokopedia.settingbank.choosebank.domain.usecase.GetBankListDBUseCase
import com.tokopedia.settingbank.choosebank.domain.usecase.GetBankListUseCase
import com.tokopedia.settingbank.choosebank.domain.usecase.GetBankListWSUseCase
import com.tokopedia.settingbank.choosebank.view.presenter.ChooseBankPresenter
import com.tokopedia.user.session.UserSession
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

        fun inject(context : Context): ChooseBankPresenter {

            val session = UserSession(context)

            val gson: Gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .setPrettyPrinting()
                    .serializeNulls().create()

            val stringResponseConverter = StringResponseConverter()

            val tkpdResponseConverter = TkpdResponseConverter()

            val generatedHostConverter = GeneratedHostConverter()

            val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
                    .addConverterFactory(generatedHostConverter)
                    .addConverterFactory(tkpdResponseConverter)
                    .addConverterFactory(stringResponseConverter)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

            val chuckInterceptor = ChuckInterceptor(context)

            val httpLoggingInterceptor = HttpLoggingInterceptor()

            if (com.tokopedia.core.util.GlobalConfig.isAllowDebuggingTools()) {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
            }

            val fingerprintInterceptor = FingerprintInterceptor()

            val abstractionSession: com.tokopedia.abstraction.common.data.model.session.UserSession = (context as AbstractionRouter).session

            val abstractionRouter: AbstractionRouter = context

            val tkpdAuthInterceptor = TkpdAuthInterceptor(context,
                    abstractionRouter, abstractionSession)

            val builder: OkHttpClient.Builder = OkHttpClient.Builder()

            if (GlobalConfig.isAllowDebuggingTools()) {
                builder.addInterceptor(chuckInterceptor)
                builder.addInterceptor(DebugInterceptor())
                builder.addInterceptor(httpLoggingInterceptor)
            }

            builder.addInterceptor(fingerprintInterceptor)
            builder.addInterceptor(tkpdAuthInterceptor)

            val okHttpClient: OkHttpClient = builder.build()

            val retrofit: Retrofit = retrofitBuilder.baseUrl(BankListUrl.BASE_URL)
                    .client(okHttpClient)
                    .build()

            val bankListApi: BankListApi = retrofit.create(BankListApi::class.java)

            val getBankListDBMapper = GetBankListDBMapper()
            val getBankListDBUseCase = GetBankListDBUseCase(getBankListDBMapper)

            val getBankListWSMapper = GetBankListWSMapper()
            val getBankListWSUseCase = GetBankListWSUseCase(bankListApi, getBankListWSMapper)

            val bankCache = LocalCacheHandler(context, HomeRouter.TAG_FETCH_BANK)

            val getBankListUseCase = GetBankListUseCase(getBankListDBUseCase,
                    getBankListWSUseCase, bankCache)
            return ChooseBankPresenter(session , getBankListUseCase)
        }
    }
}