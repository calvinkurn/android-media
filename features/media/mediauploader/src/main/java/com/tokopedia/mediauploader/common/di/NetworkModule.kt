package com.tokopedia.mediauploader.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mediauploader.common.util.MediaUploaderNetwork
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class NetworkModule {

    @Provides
    @MediaUploaderQualifier
    fun provideOkHttpClientBuilder(
        @ApplicationContext context: Context,
        @MediaUploaderQualifier userSession: UserSessionInterface
    ): OkHttpClient.Builder {
        return MediaUploaderNetwork.okHttpClientBuilder(context, userSession)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideMediaUploaderRetrofitBuilder(): Retrofit.Builder {
        return MediaUploaderNetwork.retrofitBuilder()
    }

}