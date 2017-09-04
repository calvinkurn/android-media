package com.tokopedia.posapp.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.core.TkpdV4ResponseError;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.posapp.data.factory.ShopFactory;
import com.tokopedia.posapp.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.data.mapper.GetShopMapper;
import com.tokopedia.posapp.data.repository.ShopRepository;
import com.tokopedia.posapp.data.repository.ShopRepositoryImpl;
import com.tokopedia.posapp.data.source.cloud.api.ShopApi;
import com.tokopedia.posapp.domain.usecase.GetProductListUseCase;
import com.tokopedia.posapp.domain.usecase.StoreProductCacheUseCase;
import com.tokopedia.posapp.view.presenter.CachePresenter;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by okasurya on 8/29/17.
 */

public class CacheServiceDependencies {

    public CachePresenter provideCachePresenter(Context context) {
        return new CachePresenter(
                context,
                provideGetProductListUseCase(),
                provideStoreProductCacheUseCase()
        );
    }

    private GetProductListUseCase provideGetProductListUseCase() {
        return new GetProductListUseCase(
                provideThreadExecutor(),
                providePostExecutionThread(),
                provideShopRepository(
                    provideShopFactory(
                        provideShopApi(
                            provideWsV4RetrofitWithErrorHandler(
                                provideOkHttpClient(),
                                provideRetrofitBuilder(TkpdBaseURL.ACE_DOMAIN)
                            )
                        ),
                        provideShopMapper(),
                        provideProductListMapper()
                    )
                )
        );
    }



    private StoreProductCacheUseCase provideStoreProductCacheUseCase() {
        return new StoreProductCacheUseCase(
                provideThreadExecutor(),
                providePostExecutionThread(),
                provideShopRepository(
                    provideShopFactory(
                        provideShopApi(
                            provideWsV4RetrofitWithErrorHandler(
                                provideOkHttpClient(),
                                provideRetrofitBuilder(TkpdBaseURL.BASE_DOMAIN)
                            )
                        ),
                        provideShopMapper(),
                        provideProductListMapper()
                    )
                )
        );
    }

    private ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    private PostExecutionThread providePostExecutionThread() {
        return new PostExecutionThread() {
            @Override
            public Scheduler getScheduler() {
                return Schedulers.newThread();
            }
        };
    }

    private ShopRepository provideShopRepository(ShopFactory shopFactory) {
        return new ShopRepositoryImpl(shopFactory);
    }

    private ShopFactory provideShopFactory(ShopApi shopApi, GetShopMapper getShopMapper, GetProductListMapper getProductListMapper) {
        return new ShopFactory(shopApi, getShopMapper, getProductListMapper);
    }

    private ShopApi provideShopApi(Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }

    private GetShopMapper provideShopMapper() {
        return new GetShopMapper();
    }

    private GetProductListMapper provideProductListMapper() {
        return new GetProductListMapper();
    }

    private Retrofit provideWsV4RetrofitWithErrorHandler(OkHttpClient okHttpClient,
                                                        Retrofit.Builder retrofitBuilder){
        return retrofitBuilder.client(okHttpClient).build();
    }

    private OkHttpClient provideOkHttpClient() {
        return OkHttpFactory.create().buildDaggerClientDefaultAuthWithErrorHandler(
                new FingerprintInterceptor(),
                new TkpdAuthInterceptor(),
                OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy(),
                provideChuckInterceptor(),
                new DebugInterceptor(),
                new TkpdErrorResponseInterceptor(TkpdV4ResponseError.class)
        );
    }

    private Retrofit.Builder provideRetrofitBuilder(String domain) {
        return RetrofitFactory.createRetrofitDefaultConfig(domain);
    }

    private ChuckInterceptor provideChuckInterceptor() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(MainApplication.getAppContext(), DeveloperOptions.CHUCK_ENABLED);
        return new ChuckInterceptor(MainApplication.getAppContext())
                .showNotification(localCacheHandler.getBoolean(DeveloperOptions.IS_CHUCK_ENABLED, false));
    }
}
