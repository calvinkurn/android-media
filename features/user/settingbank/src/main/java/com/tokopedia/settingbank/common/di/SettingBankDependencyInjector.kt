package com.tokopedia.settingbank.common.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.settingbank.data.SettingBankApi
import com.tokopedia.settingbank.data.SettingBankUrl
import com.tokopedia.settingbank.domain.mapper.GetBankListMapper
import com.tokopedia.settingbank.domain.usecase.GetBankListUseCase
import com.tokopedia.settingbank.view.presenter.SettingBankPresenter
import com.tokopedia.user.session.UserSession
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author by nisie on 6/8/18.
 */
class SettingBankDependencyInjector {

    object Companion {

        fun inject(context: Context): SettingBankPresenter {

            val session = UserSession(context)

            val gson: Gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .setPrettyPrinting()
                    .serializeNulls().create()

//            val stringResponseConverter = StringResponseConverter()
//
//            val tkpdResponseConverter = TkpdResponseConverter()
//
//            val generatedHostConverter = GeneratedHostConverter()

            val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
//                    .addConverterFactory(generatedHostConverter)
//                    .addConverterFactory(tkpdResponseConverter)
//                    .addConverterFactory(stringResponseConverter)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

            val builder: OkHttpClient.Builder = OkHttpClient.Builder()

            val okHttpClient: OkHttpClient = builder.build()

            val retrofit: Retrofit = retrofitBuilder.baseUrl(SettingBankUrl.BASE_URL)
                    .client(okHttpClient)
                    .build()

            val settingBankApi: SettingBankApi = retrofit.create(SettingBankApi::class.java)

            val getBankListMapper = GetBankListMapper()

            val getBankListUseCase = GetBankListUseCase(settingBankApi, getBankListMapper)

            return SettingBankPresenter(session, getBankListUseCase)
        }
    }
}