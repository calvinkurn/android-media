package com.tokopedia.profilecompletion.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imageuploader.ImageUploaderRouter
import com.tokopedia.imageuploader.data.*
import com.tokopedia.imageuploader.data.entity.ImageUploaderResponseError
import com.tokopedia.imageuploader.data.source.GenerateHostCloud
import com.tokopedia.imageuploader.data.source.GenerateHostDataSource
import com.tokopedia.imageuploader.data.source.UploadImageDataSourceCloud
import com.tokopedia.imageuploader.data.source.api.GenerateHostApi
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderAuthInterceptorQualifier
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderChuckQualifier
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.profilecompletion.data.UploadProfileImageModel
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

@Module
class ImageUploadModule {
    private val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    private val NET_READ_TIMEOUT = 100
    private val NET_WRITE_TIMEOUT = 100
    private val NET_CONNECT_TIMEOUT = 100
    private val NET_RETRY = 1

    @Provides
    @ImageUploaderQualifier
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @ImageUploaderQualifier
    fun provideFingerprintInterceptor(@ImageUploaderQualifier networkRouter: NetworkRouter,
                                      userSessionInterface: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSessionInterface)
    }

    @ImageUploaderQualifier
    @Provides
    fun okHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY)
    }

    @ImageUploaderQualifier
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   @ImageUploaderQualifier networkRouter: NetworkRouter,
                                   userSessionInterface: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ImageUploaderQualifier
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

    @ImageUploaderQualifier
    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(ImageUploaderResponseError::class.java)
    }

    @ImageUploaderQualifier
    @Provides
    fun provideOkHttpClient(@ImageUploaderQualifier tkpdAuthInterceptor: TkpdAuthInterceptor,
                            @ImageUploaderQualifier fingerprintInterceptor: FingerprintInterceptor,
                            @ImageUploaderQualifier retryPolicy: OkHttpRetryPolicy,
                            @ImageUploaderQualifier loggingInterceptor: HttpLoggingInterceptor,
                            @ImageUploaderQualifier errorHandlerInterceptor: ErrorResponseInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(tkpdAuthInterceptor)
        builder.addInterceptor(fingerprintInterceptor)
        builder.addInterceptor(errorHandlerInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(loggingInterceptor)
        }
        builder.readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
        return builder.build()
    }

    @ImageUploaderQualifier
    @Provides
    fun provideRetrofitBuilder(@ImageUploaderQualifier gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }

    @ImageUploaderQualifier
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create()
    }

    @Provides
    @ImageUploaderQualifier
    internal fun provideUploadImageDataSourceCloud(@ImageUploaderQualifier retrofit: Retrofit.Builder, @ImageUploaderQualifier okHttpClient: OkHttpClient): UploadImageDataSourceCloud {
        return UploadImageDataSourceCloud(retrofit, okHttpClient)
    }

    @Provides
    @ImageUploaderQualifier
    internal fun provideUploadImageDataSource(@ImageUploaderQualifier uploadImageDataSourceCloud: UploadImageDataSourceCloud): UploadImageDataSource {
        return UploadImageDataSource(uploadImageDataSourceCloud)
    }

    @Provides
    @ImageUploaderQualifier
    internal fun provideUploadImageRepository(@ImageUploaderQualifier uploadImageDataSourceCloud: UploadImageDataSource): UploadImageRepository {
        return UploadImageRepositoryImpl(uploadImageDataSourceCloud)
    }

    @ImageUploaderQualifier
    @Provides
    fun provideWsV4RetrofitWithErrorHandler(@ImageUploaderQualifier okHttpClient: OkHttpClient,
                                            @ImageUploaderQualifier retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ImageUploaderUrl.BASE_URL).client(okHttpClient).build()
    }

    @Provides
    @ImageUploaderQualifier
    internal fun provideGenerateHostApi(@ImageUploaderQualifier retrofit: Retrofit): GenerateHostApi {
        return retrofit.create(GenerateHostApi::class.java)
    }

    @Provides
    @ImageUploaderQualifier
    internal fun provideGenerateHostCloud(@ImageUploaderQualifier generateHostApi: GenerateHostApi,
                                          userSession: UserSessionInterface): GenerateHostCloud {
        return GenerateHostCloud(generateHostApi, userSession)
    }

    @ImageUploaderQualifier
    @Provides
    internal fun provideGenerateHostDataSource(@ImageUploaderQualifier generateHostCloud: GenerateHostCloud): GenerateHostDataSource {
        return GenerateHostDataSource(generateHostCloud)
    }


    @Provides
    @ImageUploaderQualifier
    internal fun provideGenerateHostRepository(@ImageUploaderQualifier generateHostDataSource: GenerateHostDataSource): GenerateHostRepository {
        return GenerateHostRepositoryImpl(generateHostDataSource)
    }

    @Provides
    @ImageUploaderQualifier
    internal fun provideImageUploaderUtils( userSession: UserSessionInterface): ImageUploaderUtils {
        return ImageUploaderUtils(userSession)
    }

    @Provides
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            userSession: UserSessionInterface,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<UploadProfileImageModel> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession, UploadProfileImageModel::class.java, imageUploaderUtils)
    }
}