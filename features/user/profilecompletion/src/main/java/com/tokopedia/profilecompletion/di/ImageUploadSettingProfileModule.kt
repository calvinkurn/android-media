package com.tokopedia.profilecompletion.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imageuploader.data.*
import com.tokopedia.imageuploader.data.entity.ImageUploaderResponseError
import com.tokopedia.imageuploader.data.source.GenerateHostCloud
import com.tokopedia.imageuploader.data.source.GenerateHostDataSource
import com.tokopedia.imageuploader.data.source.UploadImageDataSourceCloud
import com.tokopedia.imageuploader.data.source.api.GenerateHostApi
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.profilecompletion.data.UploadProfileImageModel
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class ImageUploadSettingProfileModule {
    private val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    private val NET_READ_TIMEOUT = 100
    private val NET_WRITE_TIMEOUT = 100
    private val NET_CONNECT_TIMEOUT = 100
    private val NET_RETRY = 1

    @Provides
    @ProfileCompletionQualifier
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @ProfileCompletionQualifier
    fun provideFingerprintInterceptor(@ProfileCompletionQualifier networkRouter: NetworkRouter,
                                      userSessionInterface: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSessionInterface)
    }

    @ProfileCompletionQualifier
    @Provides
    fun okHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY)
    }

    @ProfileCompletionQualifier
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   @ProfileCompletionQualifier networkRouter: NetworkRouter,
                                   userSessionInterface: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ProfileCompletionQualifier
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

    @ProfileCompletionQualifier
    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(ImageUploaderResponseError::class.java)
    }

    @ProfileCompletionQualifier
    @Provides
    fun provideOkHttpClient(@ProfileCompletionQualifier tkpdAuthInterceptor: TkpdAuthInterceptor,
                            @ProfileCompletionQualifier fingerprintInterceptor: FingerprintInterceptor,
                            @ProfileCompletionQualifier retryPolicy: OkHttpRetryPolicy,
                            @ProfileCompletionQualifier loggingInterceptor: HttpLoggingInterceptor,
                            @ProfileCompletionQualifier errorHandlerInterceptor: ErrorResponseInterceptor): OkHttpClient {
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

    @ProfileCompletionQualifier
    @Provides
    fun provideRetrofitBuilder(@ProfileCompletionQualifier gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }

    @ProfileCompletionQualifier
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create()
    }

    @Provides
    @ProfileCompletionQualifier
    fun provideUploadImageDataSourceCloud(@ProfileCompletionQualifier retrofit: Retrofit.Builder, @ProfileCompletionQualifier okHttpClient: OkHttpClient): UploadImageDataSourceCloud {
        return UploadImageDataSourceCloud(retrofit, okHttpClient)
    }

    @Provides
    @ProfileCompletionQualifier
    fun provideUploadImageDataSource(@ProfileCompletionQualifier uploadImageDataSourceCloud: UploadImageDataSourceCloud): UploadImageDataSource {
        return UploadImageDataSource(uploadImageDataSourceCloud)
    }

    @Provides
    @ProfileCompletionQualifier
    fun provideUploadImageRepository(@ProfileCompletionQualifier uploadImageDataSourceCloud: UploadImageDataSource): UploadImageRepository {
        return UploadImageRepositoryImpl(uploadImageDataSourceCloud)
    }

    @ProfileCompletionQualifier
    @Provides
    fun provideWsV4RetrofitWithErrorHandler(@ProfileCompletionQualifier okHttpClient: OkHttpClient,
                                            @ProfileCompletionQualifier retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ImageUploaderUrl.BASE_URL).client(okHttpClient).build()
    }

    @Provides
    @ProfileCompletionQualifier
    fun provideGenerateHostApi(@ProfileCompletionQualifier retrofit: Retrofit): GenerateHostApi {
        return retrofit.create(GenerateHostApi::class.java)
    }

    @Provides
    @ProfileCompletionQualifier
    fun provideGenerateHostCloud(@ProfileCompletionQualifier generateHostApi: GenerateHostApi,
                                          userSession: UserSessionInterface): GenerateHostCloud {
        return GenerateHostCloud(generateHostApi, userSession)
    }

    @ProfileCompletionQualifier
    @Provides
    fun provideGenerateHostDataSource(@ProfileCompletionQualifier generateHostCloud: GenerateHostCloud): GenerateHostDataSource {
        return GenerateHostDataSource(generateHostCloud)
    }


    @Provides
    @ProfileCompletionQualifier
    fun provideGenerateHostRepository(@ProfileCompletionQualifier generateHostDataSource: GenerateHostDataSource): GenerateHostRepository {
        return GenerateHostRepositoryImpl(generateHostDataSource)
    }

    @Provides
    @ProfileCompletionQualifier
    fun provideImageUploaderUtils( userSession: UserSessionInterface): ImageUploaderUtils {
        return ImageUploaderUtils(userSession)
    }

    @ProfileCompletionQualifier
    @Provides
    fun provideUploadImageUseCase(
            @ProfileCompletionQualifier uploadImageRepository: UploadImageRepository,
            @ProfileCompletionQualifier generateHostRepository: GenerateHostRepository,
            @ProfileCompletionQualifier gson: Gson,
            userSession: UserSessionInterface,
            @ProfileCompletionQualifier imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<UploadProfileImageModel> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession, UploadProfileImageModel::class.java, imageUploaderUtils)
    }
}