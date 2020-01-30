package com.tokopedia.imageuploader.di;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.imageuploader.data.GenerateHostRepositoryImpl;
import com.tokopedia.imageuploader.data.ImageUploaderUrl;
import com.tokopedia.imageuploader.data.ProgressResponseBody;
import com.tokopedia.imageuploader.data.StringResponseConverter;
import com.tokopedia.imageuploader.data.UploadImageDataSource;
import com.tokopedia.imageuploader.data.UploadImageRepositoryImpl;
import com.tokopedia.imageuploader.data.entity.ImageUploaderResponseError;
import com.tokopedia.imageuploader.data.source.GenerateHostCloud;
import com.tokopedia.imageuploader.data.source.GenerateHostDataSource;
import com.tokopedia.imageuploader.data.source.UploadImageDataSourceCloud;
import com.tokopedia.imageuploader.data.source.api.GenerateHostApi;
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderChuckQualifier;
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier;
import com.tokopedia.imageuploader.domain.GenerateHostRepository;
import com.tokopedia.imageuploader.domain.UploadImageRepository;
import com.tokopedia.imageuploader.utils.ImageUploaderUtils;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ImageUploaderModule {
    private static final String GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final int NET_READ_TIMEOUT = 100;
    private static final int NET_WRITE_TIMEOUT = 100;
    private static final int NET_CONNECT_TIMEOUT = 100;
    private static final int NET_RETRY = 1;

    private boolean isNeedProgress = false;
    private ProgressResponseBody.ProgressListener progressListener;

    public ImageUploaderModule() {
    }

    public ImageUploaderModule(ProgressResponseBody.ProgressListener progressListener) {
        this.isNeedProgress = true;
        this.progressListener = progressListener;
    }

    @Provides
    @ImageUploaderQualifier
    public NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return ((NetworkRouter) context);
    }

    @Provides
    @ImageUploaderQualifier
    public FingerprintInterceptor provideFingerprintInterceptor(@ImageUploaderQualifier NetworkRouter networkRouter,
                                                                @ImageUploaderQualifier UserSessionInterface userSessionInterface) {
        return new FingerprintInterceptor(networkRouter, userSessionInterface);
    }

    @ImageUploaderQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(@ImageUploaderQualifier TkpdAuthInterceptor tkpdAuthInterceptor,
                                            @ImageUploaderQualifier FingerprintInterceptor fingerprintInterceptor,
                                            @ImageUploaderQualifier OkHttpRetryPolicy retryPolicy,
                                            @ImageUploaderChuckQualifier Interceptor chuckInterceptor,
                                            @ImageUploaderQualifier HttpLoggingInterceptor loggingInterceptor,
                                            @ImageUploaderQualifier ErrorResponseInterceptor errorHandlerInterceptor,
                                            @ImageUploaderQualifier CacheApiInterceptor cacheApiInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(tkpdAuthInterceptor);
        builder.addInterceptor(fingerprintInterceptor);
        builder.addInterceptor(cacheApiInterceptor);
        builder.addInterceptor(errorHandlerInterceptor);

        if (isNeedProgress) {
            builder.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();
                }
            });
        }

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(loggingInterceptor);
            if (chuckInterceptor != null) {
                builder.addInterceptor(chuckInterceptor);
            }
        }
        builder.readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS);
        builder.connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS);
        builder.writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS);
        return builder.build();
    }

    @ImageUploaderQualifier
    @Provides
    public OkHttpRetryPolicy okHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY);
    }

    @ImageUploaderQualifier
    @Provides
    public TkpdAuthInterceptor provideTkpdAuthInterceptor(@ImageUploaderQualifier Context context,
                                                          @ImageUploaderQualifier NetworkRouter networkRouter,
                                                          @ImageUploaderQualifier UserSessionInterface userSessionInterface) {
        return new TkpdAuthInterceptor(context, networkRouter, userSessionInterface);
    }

    @ImageUploaderQualifier
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ImageUploaderQualifier
    @Provides
    public Context provideContext(@ApplicationContext Context context) {
        return context;
    }

    @ImageUploaderChuckQualifier
    @Provides
    public Interceptor provideInterceptor(@ImageUploaderQualifier Context context) {
        return new ChuckInterceptor(context);
    }

    @ImageUploaderQualifier
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logging;
    }

    @ImageUploaderQualifier
    @Provides
    public ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(ImageUploaderResponseError.class);
    }

    @ImageUploaderQualifier
    @Provides
    public CacheApiInterceptor provideCacheApiInterceptor(@ApplicationContext Context context) {
        return new CacheApiInterceptor(context);
    }

    @ImageUploaderQualifier
    @Provides
    public Retrofit provideWsV4RetrofitWithErrorHandler(@ImageUploaderQualifier OkHttpClient okHttpClient,
                                                        @ImageUploaderQualifier Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ImageUploaderUrl.BASE_URL).client(okHttpClient).build();
    }

    @Provides
    @ImageUploaderQualifier
    GenerateHostApi provideGenerateHostApi(@ImageUploaderQualifier Retrofit retrofit) {
        return retrofit.create(GenerateHostApi.class);
    }

    @Provides
    @ImageUploaderQualifier
    GenerateHostCloud provideGenerateHostCloud(@ImageUploaderQualifier GenerateHostApi generateHostApi,
                                               @ImageUploaderQualifier UserSessionInterface userSession) {
        return new GenerateHostCloud(generateHostApi, userSession);
    }

    @ImageUploaderQualifier
    @Provides
    GenerateHostDataSource provideGenerateHostDataSource(@ImageUploaderQualifier GenerateHostCloud generateHostCloud) {
        return new GenerateHostDataSource(generateHostCloud);
    }


    @Provides
    @ImageUploaderQualifier
    GenerateHostRepository provideGenerateHostRepository(@ImageUploaderQualifier GenerateHostDataSource generateHostDataSource) {
        return new GenerateHostRepositoryImpl(generateHostDataSource);
    }

    @Provides
    @ImageUploaderQualifier
    UploadImageDataSourceCloud provideUploadImageDataSourceCloud(@ImageUploaderQualifier Retrofit.Builder retrofit, @ImageUploaderQualifier OkHttpClient okHttpClient) {
        return new UploadImageDataSourceCloud(retrofit, okHttpClient);
    }

    @Provides
    @ImageUploaderQualifier
    UploadImageDataSource provideUploadImageDataSource(@ImageUploaderQualifier UploadImageDataSourceCloud uploadImageDataSourceCloud) {
        return new UploadImageDataSource(uploadImageDataSourceCloud);
    }

    @Provides
    @ImageUploaderQualifier
    UploadImageRepository provideUploadImageRepository(@ImageUploaderQualifier UploadImageDataSource uploadImageDataSourceCloud) {
        return new UploadImageRepositoryImpl(uploadImageDataSourceCloud);
    }

    @Provides
    @ImageUploaderQualifier
    ImageUploaderUtils provideImageUploaderUtils(@ImageUploaderQualifier UserSessionInterface userSession) {
        return new ImageUploaderUtils(userSession);
    }

    @ImageUploaderQualifier
    @Provides
    public Retrofit.Builder provideRetrofitBuilder(@ImageUploaderQualifier Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    @ImageUploaderQualifier
    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }
}
