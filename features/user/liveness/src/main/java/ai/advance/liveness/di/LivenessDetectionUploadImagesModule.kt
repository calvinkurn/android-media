package ai.advance.liveness.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imageuploader.data.StringResponseConverter
import com.tokopedia.imageuploader.data.entity.ImageUploaderResponseError
import com.tokopedia.network.CoroutineCallAdapterFactory
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class LivenessDetectionUploadImagesModule {

    private val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    private val NET_READ_TIMEOUT = 25
    private val NET_WRITE_TIMEOUT = 25
    private val NET_CONNECT_TIMEOUT = 25
    private val NET_RETRY = 3

    @LivenessDetectionScope
    @LivenessDetectionQualifier
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

    @LivenessDetectionScope
    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(ImageUploaderResponseError::class.java)
    }

    @LivenessDetectionScope
    @Provides
    fun provideWsV4RetrofitWithErrorHandler(okHttpClient: OkHttpClient,
                                            retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl("http://172.18.1.28/").client(okHttpClient).build()
    }

    @LivenessDetectionScope
    @Provides
    fun okHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY)
    }

    @LivenessDetectionScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @LivenessDetectionScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSessionInterface: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @LivenessDetectionScope
    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context).showNotification(com.tokopedia.abstraction.common.utils.GlobalConfig.isAllowDebuggingTools())
    }

    @LivenessDetectionScope
    @Provides
    fun provideOkHttpClient(tkpdAuthInterceptor: TkpdAuthInterceptor,
                            retryPolicy: OkHttpRetryPolicy,
                            loggingInterceptor: HttpLoggingInterceptor,
                            errorHandlerInterceptor: ErrorResponseInterceptor,
                            chuckInterceptor: ChuckInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(errorHandlerInterceptor)
        builder.addInterceptor(tkpdAuthInterceptor)
        builder.addInterceptor(chuckInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        builder.readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
        return builder.build()
    }

    @LivenessDetectionScope
    @LivenessDetectionQualifier
    @Provides
    fun provideRetrofitBuilder(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
    }

    @LivenessDetectionScope
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create()
    }
}