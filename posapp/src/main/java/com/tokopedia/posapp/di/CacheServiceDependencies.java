package com.tokopedia.posapp.di;

/**
 * Created by okasurya on 8/29/17.
 */

public class CacheServiceDependencies {

//    public CachePresenter provideCachePresenter(Context context) {
//        return new CachePresenter(
//                context,
//                provideGetProductListUseCase(),
//                provideStoreProductCacheUseCase()
//        );
//    }
//
//    private GetProductListUseCase provideGetProductListUseCase() {
//        return new GetProductListUseCase(
//                provideThreadExecutor(),
//                providePostExecutionThread(),
//                provideShopRepository(
//                    provideShopFactory(
//                        provideShopApi(
//                            provideWsV4RetrofitWithErrorHandler(
//                                provideOkHttpClient(),
//                                provideRetrofitBuilder(TkpdBaseURL.ACE_DOMAIN)
//                            )
//                        ),
//                        provideShopMapper(),
//                        provideProductListMapper()
//                    )
//                )
//        );
//    }
//
//
//
//    private StoreProductCacheUseCase provideStoreProductCacheUseCase() {
//        return new StoreProductCacheUseCase(
//                provideThreadExecutor(),
//                providePostExecutionThread(),
//                provideShopRepository(
//                    provideShopFactory(
//                        provideShopApi(
//                            provideWsV4RetrofitWithErrorHandler(
//                                provideOkHttpClient(),
//                                provideRetrofitBuilder(TkpdBaseURL.BASE_DOMAIN)
//                            )
//                        ),
//                        provideShopMapper(),
//                        provideProductListMapper()
//                    )
//                )
//        );
//    }
//
//    private ThreadExecutor provideThreadExecutor() {
//        return new JobExecutor();
//    }
//
//    private PostExecutionThread providePostExecutionThread() {
//        return new PostExecutionThread() {
//            @Override
//            public Scheduler getScheduler() {
//                return Schedulers.newThread();
//            }
//        };
//    }
//
//    private ShopRepository provideShopRepository(ShopFactory shopFactory) {
//        return new ShopRepositoryImpl(shopFactory);
//    }
//
//    private ShopFactory provideShopFactory(ShopApi shopApi, GetShopMapper getShopMapper, GetShopProductMapper getProductListMapper) {
//        return new ShopFactory(shopApi, getShopMapper, getProductListMapper);
//    }
//
//    private ShopApi provideShopApi(Retrofit retrofit) {
//        return retrofit.create(ShopApi.class);
//    }
//
//    private GetShopMapper provideShopMapper() {
//        return new GetShopMapper();
//    }
//
//    private GetShopProductMapper provideProductListMapper() {
//        return new GetShopProductMapper();
//    }
//
//    private Retrofit provideWsV4RetrofitWithErrorHandler(OkHttpClient okHttpClient,
//                                                        Retrofit.Builder retrofitBuilder){
//        return retrofitBuilder.client(okHttpClient).build();
//    }
//
//    private OkHttpClient provideOkHttpClient() {
//        return OkHttpFactory.create().buildDaggerClientDefaultAuthWithErrorHandler(
//                new FingerprintInterceptor(),
//                new TkpdAuthInterceptor(),
//                OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy(),
//                provideChuckInterceptor(),
//                new DebugInterceptor(),
//                new TkpdErrorResponseInterceptor(TkpdV4ResponseError.class)
//        );
//    }
//
//    private Retrofit.Builder provideRetrofitBuilder(String domain) {
//        return RetrofitFactory.createRetrofitDefaultConfig(domain);
//    }
//
//    private ChuckInterceptor provideChuckInterceptor() {
//        LocalCacheHandler localCacheHandler = new LocalCacheHandler(MainApplication.getAppContext(), DeveloperOptions.CHUCK_ENABLED);
//        return new ChuckInterceptor(MainApplication.getAppContext())
//                .showNotification(localCacheHandler.getBoolean(DeveloperOptions.IS_CHUCK_ENABLED, false));
//    }
}
