package com.tokopedia.changepassword.common.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.changepassword.data.ChangePasswordApi
import com.tokopedia.changepassword.data.ChangePasswordUrl
import com.tokopedia.changepassword.domain.ChangePasswordUseCase
import com.tokopedia.changepassword.domain.mapper.ChangePasswordMapper
import com.tokopedia.changepassword.view.presenter.ChangePasswordPresenter
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author by nisie on 7/25/18.
 */
class ChangePasswordDependencyInjector {

    object Companion {
        val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"

        fun inject(context: Context): ChangePasswordPresenter {

            val userSession : UserSessionInterface = UserSession(context)

            val gson: Gson = GsonBuilder()
                    .setDateFormat(DATE_FORMAT)
                    .setPrettyPrinting()
                    .serializeNulls().create()

            val stringResponseConverter = StringResponseConverter()

            val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
                    .addConverterFactory(stringResponseConverter)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

            val chuckInterceptor = ChuckerInterceptor(context)

            val httpLoggingInterceptor = HttpLoggingInterceptor().apply{
                level = if (GlobalConfig.isAllowDebuggingTools()) { HttpLoggingInterceptor.Level.BODY} else { HttpLoggingInterceptor.Level.NONE}
            }

            val networkRouter = context as NetworkRouter
            val fingerprintInterceptor = FingerprintInterceptor(networkRouter, userSession)

            val tkpdAuthInterceptor = TkpdAuthInterceptor(context,
                    networkRouter, userSession)

            val builder: OkHttpClient.Builder = OkHttpClient.Builder()

            builder.addInterceptor(fingerprintInterceptor)
            builder.addInterceptor(tkpdAuthInterceptor)

            if (GlobalConfig.isAllowDebuggingTools()) {
                builder.addInterceptor(chuckInterceptor)
                builder.addInterceptor(DebugInterceptor())
                builder.addInterceptor(httpLoggingInterceptor)
            }

            val okHttpClient: OkHttpClient = builder.build()

            val retrofit: Retrofit = retrofitBuilder.baseUrl(ChangePasswordUrl.BASE_URL)
                    .client(okHttpClient)
                    .build()

            val changePasswordApi: ChangePasswordApi = retrofit.create(ChangePasswordApi::class.java)

            val changePasswordMapper = ChangePasswordMapper()

            val changePasswordUseCase = ChangePasswordUseCase(changePasswordApi,
                    changePasswordMapper)


            return ChangePasswordPresenter(changePasswordUseCase, userSession)
        }
    }
}