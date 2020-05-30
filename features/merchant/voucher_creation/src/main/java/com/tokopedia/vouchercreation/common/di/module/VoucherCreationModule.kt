package com.tokopedia.vouchercreation.common.di.module

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.imageuploader.data.GenerateHostRepositoryImpl
import com.tokopedia.imageuploader.data.ImageUploaderUrl
import com.tokopedia.imageuploader.data.UploadImageDataSource
import com.tokopedia.imageuploader.data.UploadImageRepositoryImpl
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
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.common.di.scope.VoucherCreationScope
import com.tokopedia.vouchercreation.create.domain.model.upload.ImageUploadResponse
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class VoucherCreationModule {

    companion object {
        private const val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
        private const val NET_READ_TIMEOUT = 100
        private const val NET_WRITE_TIMEOUT = 100
        private const val NET_CONNECT_TIMEOUT = 100
        private const val NET_RETRY = 1
    }

    @Named("Main")
    @VoucherCreationScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @VoucherCreationScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @VoucherCreationScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)


    @Provides
    @VoucherCreationScope
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @VoucherCreationScope
    fun provideFingerprintInterceptor(@VoucherCreationScope networkRouter: NetworkRouter,
                                      userSessionInterface: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSessionInterface)
    }

    @VoucherCreationScope
    @Provides
    fun okHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY)
    }

    @VoucherCreationScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   @VoucherCreationScope networkRouter: NetworkRouter,
                                   userSessionInterface: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @VoucherCreationScope
    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(ImageUploaderResponseError::class.java)
    }

    @VoucherCreationScope
    @Provides
    fun provideOkHttpClient(@VoucherCreationScope tkpdAuthInterceptor: TkpdAuthInterceptor,
                            @VoucherCreationScope fingerprintInterceptor: FingerprintInterceptor,
                            @VoucherCreationScope retryPolicy: OkHttpRetryPolicy,
                            @ApplicationScope loggingInterceptor: HttpLoggingInterceptor,
                            @VoucherCreationScope errorHandlerInterceptor: ErrorResponseInterceptor): OkHttpClient {
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

    @Provides
    @VoucherCreationScope
    fun provideUploadImageDataSourceCloud(@ApplicationScope retrofit: Retrofit.Builder, @VoucherCreationScope okHttpClient: OkHttpClient): UploadImageDataSourceCloud {
        return UploadImageDataSourceCloud(retrofit, okHttpClient)
    }

    @Provides
    @VoucherCreationScope
    fun provideUploadImageDataSource(@VoucherCreationScope uploadImageDataSourceCloud: UploadImageDataSourceCloud): UploadImageDataSource {
        return UploadImageDataSource(uploadImageDataSourceCloud)
    }

    @Provides
    @VoucherCreationScope
    fun provideUploadImageRepository(@VoucherCreationScope uploadImageDataSourceCloud: UploadImageDataSource): UploadImageRepository {
        return UploadImageRepositoryImpl(uploadImageDataSourceCloud)
    }

    @VoucherCreationScope
    @Provides
    fun provideWsV4RetrofitWithErrorHandler(@VoucherCreationScope okHttpClient: OkHttpClient,
                                            @VoucherCreationScope retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ImageUploaderUrl.BASE_URL).client(okHttpClient).build()
    }

    @Provides
    @VoucherCreationScope
    fun provideGenerateHostApi(@VoucherCreationScope retrofit: Retrofit): GenerateHostApi {
        return retrofit.create(GenerateHostApi::class.java)
    }

    @Provides
    @VoucherCreationScope
    fun provideGenerateHostCloud(@VoucherCreationScope generateHostApi: GenerateHostApi,
                                 userSession: UserSessionInterface): GenerateHostCloud {
        return GenerateHostCloud(generateHostApi, userSession)
    }

    @VoucherCreationScope
    @Provides
    fun provideGenerateHostDataSource(@VoucherCreationScope generateHostCloud: GenerateHostCloud): GenerateHostDataSource {
        return GenerateHostDataSource(generateHostCloud)
    }


    @Provides
    @VoucherCreationScope
    fun provideGenerateHostRepository(@VoucherCreationScope generateHostDataSource: GenerateHostDataSource): GenerateHostRepository {
        return GenerateHostRepositoryImpl(generateHostDataSource)
    }

    @Provides
    @VoucherCreationScope
    fun provideImageUploaderUtils(userSession: UserSessionInterface): ImageUploaderUtils {
        return ImageUploaderUtils(userSession)
    }

    @VoucherCreationScope
    @Provides
    fun provideUploadImageUseCase(
            @VoucherCreationScope uploadImageRepository: UploadImageRepository,
            @VoucherCreationScope generateHostRepository: GenerateHostRepository,
            @ApplicationScope gson: Gson,
            userSession: UserSessionInterface,
            @VoucherCreationScope imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<ImageUploadResponse> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession, ImageUploadResponse::class.java, imageUploaderUtils)
    }
}