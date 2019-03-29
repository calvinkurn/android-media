package com.tokopedia.settingbank.addeditaccount.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.settingbank.addeditaccount.domain.mapper.AddBankMapper
import com.tokopedia.settingbank.addeditaccount.domain.mapper.EditBankMapper
import com.tokopedia.settingbank.addeditaccount.domain.mapper.ValidateBankMapper
import com.tokopedia.settingbank.addeditaccount.domain.usecase.AddBankUseCase
import com.tokopedia.settingbank.addeditaccount.domain.usecase.EditBankUseCase
import com.tokopedia.settingbank.addeditaccount.domain.usecase.ValidateBankUseCase
import com.tokopedia.settingbank.addeditaccount.view.presenter.AddEditBankPresenter
import com.tokopedia.settingbank.banklist.data.SettingBankApi
import com.tokopedia.settingbank.banklist.data.SettingBankUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author by nisie on 6/22/18.
 */

class AddEditBankDependencyInjector {

    object Companion {
        fun inject(context: Context): AddEditBankPresenter {
            val session : UserSessionInterface = UserSession(context)

            val gson: Gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .setPrettyPrinting()
                    .serializeNulls().create()

            val stringResponseConverter = StringResponseConverter()

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

            val fingerprintInterceptor = FingerprintInterceptor(networkRouter, session)

            val tkpdAuthInterceptor = TkpdAuthInterceptor(context,
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

            val addBankMapper = AddBankMapper()

            val addBankUseCase = AddBankUseCase(settingBankApi, addBankMapper)

            val editBankMapper = EditBankMapper()

            val editBankUseCase = EditBankUseCase(settingBankApi, editBankMapper)

            val validateBankMapper = ValidateBankMapper()

            val validateBankUserCase = ValidateBankUseCase(settingBankApi, validateBankMapper)

            return AddEditBankPresenter(session, addBankUseCase, editBankUseCase, validateBankUserCase)
        }
    }
}