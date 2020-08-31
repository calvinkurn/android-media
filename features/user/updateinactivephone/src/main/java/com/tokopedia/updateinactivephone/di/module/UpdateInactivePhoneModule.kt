package com.tokopedia.updateinactivephone.di.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.imageuploader.data.StringResponseConverter
import com.tokopedia.imageuploader.data.entity.ImageUploaderResponseError
import com.tokopedia.network.CoroutineCallAdapterFactory
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneURL
import com.tokopedia.updateinactivephone.data.repository.UploadImageRepositoryImpl
import com.tokopedia.updateinactivephone.di.UpdateInActiveQualifier
import com.tokopedia.updateinactivephone.di.UpdateInactivePhoneScope
import com.tokopedia.updateinactivephone.usecase.*
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module class UpdateInactivePhoneModule(val context: Context) {

    private val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    private val NET_READ_TIMEOUT = 100
    private val NET_WRITE_TIMEOUT = 100
    private val NET_CONNECT_TIMEOUT = 100
    private val NET_RETRY = 3

    @Provides
    @UpdateInActiveQualifier
    fun provideInActivePhoneContext() = context

    @Provides
    @UpdateInactivePhoneScope
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @UpdateInactivePhoneScope
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @UpdateInactivePhoneScope
    fun provideUploadImageUseCase(uploadImageRepository: UploadImageRepositoryImpl): UploadImageUseCase {
        return UploadImageUseCase(context, uploadImageRepository)
    }

    @Provides
    @UpdateInactivePhoneScope
    fun provideGetUploadHostUseCase(uploadImageRepository: UploadImageRepositoryImpl): GetUploadHostUseCase {
        return GetUploadHostUseCase(uploadImageRepository)
    }

    @Provides
    @UpdateInactivePhoneScope
    fun provideWsV4RetrofitWithErrorHandler(okHttpClient: OkHttpClient,
                                            retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(UpdateInactivePhoneURL.ACCOUNTS_DOMAIN).client(okHttpClient).build()
    }

    @Provides
    @UpdateInactivePhoneScope
    fun okHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY)
    }

    @UpdateInactivePhoneScope
    @UpdateInActiveQualifier
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }

    @UpdateInactivePhoneScope
    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(ImageUploaderResponseError::class.java)
    }

    @UpdateInactivePhoneScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @UpdateInactivePhoneScope
    @Provides
    fun provideOkHttpClient(retryPolicy: OkHttpRetryPolicy,
                            loggingInterceptor: HttpLoggingInterceptor,
                            errorHandlerInterceptor: ErrorResponseInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(errorHandlerInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        builder.readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
        return builder.build()
    }

    @UpdateInactivePhoneScope
    @UpdateInActiveQualifier
    @Provides
    fun provideRetrofitBuilder(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
    }

    @UpdateInactivePhoneScope
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create()
    }

    @UpdateInactivePhoneScope
    @Provides
    fun provideUserSessionInterface(@UpdateInActiveQualifier context: Context): UserSessionInterface = UserSession(context)

}
