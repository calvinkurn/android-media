package com.tokopedia.purchase_platform.common.di

import android.content.Context
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.purchase_platform.features.checkout.data.repository.AddressRepository
import com.tokopedia.purchase_platform.features.checkout.data.repository.AddressRepositoryImpl
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Irfan Khoirul on 2019-08-28.
 */

@Module
class PeopleAddressNetworkModule {

    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context, userSessionInterface: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, context as NetworkRouter, userSessionInterface)
    }

    @Provides
    @PeopleAddressQualifier
    fun providePeopleApiOkHttpClient(@ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                                     tkpdAuthInterceptor: TkpdAuthInterceptor,
                                     okHttpRetryPolicy: OkHttpRetryPolicy,
                                     fingerprintInterceptor: FingerprintInterceptor,
                                     chuckInterceptor: ChuckInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor)
        }
        return builder.build()
    }

    @Provides
    @PeopleAddressQualifier
    fun providePeopleApiRetrofit(@PeopleAddressQualifier okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(TokopediaUrl.getInstance().WS)
                .addConverterFactory(TokopediaWsV4ResponseConverter())
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Provides
    fun providePeopleApi(@PeopleAddressQualifier retrofit: Retrofit): PeopleActApi {
        return retrofit.create(PeopleActApi::class.java)
    }

    @Provides
    fun providePeopleAddressRepository(peopleActApi: PeopleActApi): AddressRepository {
        return AddressRepositoryImpl(peopleActApi)
    }

}