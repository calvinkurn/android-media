package com.tokopedia.digital.categorylist.di;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.digital.categorylist.data.cloud.DigitalCategoryListApi;
import com.tokopedia.digital.categorylist.data.mapper.CategoryDigitalListDataMapper;
import com.tokopedia.digital.categorylist.data.repository.DigitalCategoryListRepository;
import com.tokopedia.digital.categorylist.domain.IDigitalCategoryListRepository;
import com.tokopedia.digital.categorylist.domain.interactor.DigitalCategoryListInteractor;
import com.tokopedia.digital.categorylist.domain.interactor.IDigitalCategoryListInteractor;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;
import com.tokopedia.network.utils.OkHttpRetryPolicy;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.subscriptions.CompositeSubscription;

@Module
public class DigitalListModule {
    private CompositeSubscription compositeSubscription;

    public DigitalListModule(CompositeSubscription compositeSubscription) {
        this.compositeSubscription = compositeSubscription;
    }

    public DigitalListModule() {
        this.compositeSubscription = new CompositeSubscription();
    }

    @Provides
    CategoryDigitalListDataMapper provideCategoryDigitalListDataMapper() {
        return new CategoryDigitalListDataMapper();
    }

    @Provides
    DigitalCategoryListApi provideDigitalCategoryListApi(@ApplicationContext Context context) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(TkpdBaseURL.DIGITAL_API_DOMAIN + TkpdBaseURL.DigitalApi.VERSION)
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        OkHttpClient.Builder okHttpbuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.NONE;
        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingLevel = HttpLoggingInterceptor.Level.BODY;
        }
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(loggingLevel);
        okHttpbuilder.addInterceptor(loggingInterceptor);

        NetworkRouter networkRouter = (NetworkRouter) context;
        UserSessionInterface userSessionInterceptor = new UserSession(context);

        okHttpbuilder.addInterceptor(new FingerprintInterceptor(networkRouter, userSessionInterceptor));
        okHttpbuilder.addInterceptor(new TkpdBaseInterceptor());
        OkHttpRetryPolicy okHttpRetryPolicy = OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
        okHttpbuilder.readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS);
        Retrofit retrofit = retrofitBuilder.client(okHttpbuilder.build()).build();

        return retrofit.create(DigitalCategoryListApi.class);
    }

    @Provides
    IDigitalCategoryListRepository provideIDigitalCategoryListRepository(
            DigitalCategoryListApi digitalCategoryListApi,
            CacheManager cacheManager,
            CategoryDigitalListDataMapper categoryDigitalListDataMapper,
            UserSession userSession
    ) {
        return new DigitalCategoryListRepository(
                digitalCategoryListApi,
                cacheManager,
                categoryDigitalListDataMapper,
                userSession
        );
    }

    @Provides
    IDigitalCategoryListInteractor provideDigitalCategoryListInteractor(
            IDigitalCategoryListRepository digitalCategoryListRepository,
            DigitalModuleRouter digitalModuleRouter
    ) {
        return new DigitalCategoryListInteractor(compositeSubscription, digitalCategoryListRepository, digitalModuleRouter);
    }
}
