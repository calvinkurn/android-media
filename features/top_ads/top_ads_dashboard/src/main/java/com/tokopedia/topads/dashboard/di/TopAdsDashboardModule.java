package com.tokopedia.topads.dashboard.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.product.manage.item.common.data.source.cloud.ShopApi;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.data.repository.ShopCommonRepositoryImpl;
import com.tokopedia.shop.common.data.source.ShopCommonDataSource;
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonWSApi;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;
import com.tokopedia.topads.common.data.api.TopAdsManagementApi;
import com.tokopedia.topads.common.data.repository.TopAdsShopDepositRepositoryImpl;
import com.tokopedia.topads.common.data.source.ShopDepositDataSource;
import com.tokopedia.topads.common.data.source.cloud.ShopDepositDataSourceCloud;
import com.tokopedia.topads.common.domain.repository.TopAdsShopDepositRepository;
import com.tokopedia.topads.common.data.source.local.TopAdsCacheDataSource;
import com.tokopedia.topads.common.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.topads.dashboard.TopAdsDashboardRouter;
import com.tokopedia.topads.dashboard.TopAdsDashboardTracking;
import com.tokopedia.topads.dashboard.data.repository.TopAdsDashboardRepositoryImpl;
import com.tokopedia.topads.dashboard.data.source.TopAdsDashboardDataSource;
import com.tokopedia.topads.dashboard.data.source.cloud.TopAdsDashboardDataSourceCloud;
import com.tokopedia.topads.dashboard.data.source.cloud.serviceapi.TopAdsDashboardApi;
import com.tokopedia.topads.dashboard.domain.interactor.DeleteTopAdsStatisticsUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.DeleteTopAdsTotalAdUseCase;
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractorImpl;
import com.tokopedia.topads.dashboard.domain.repository.TopAdsDashboardRepository;
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal;
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by hadi.putra on 23/04/18.
 */

@TopAdsDashboardScope
@Module
public class TopAdsDashboardModule {

    @Provides
    @TopAdsDashboardScope
    public TopAdsManagementApi provideTopAdsManagementApi(@TopAdsDashboardQualifier Retrofit retrofit){
        return retrofit.create(TopAdsManagementApi.class);
    }

    @ShopWsQualifier
    @TopAdsDashboardScope
    @Provides
    public Retrofit provideWSRetrofit(@ShopQualifier OkHttpClient okHttpClient,
                                      Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_WS_URL).client(okHttpClient).build();
    }

    @TopAdsDashboardScope
    @Provides
    public ShopApi provideShopApi(@ShopWsQualifier Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

    @Provides
    @TopAdsDashboardScope
    public ShopCommonApi provideShopCommonApi(@ShopQualifier Retrofit retrofit) {
        return retrofit.create(ShopCommonApi.class);
    }

    @Provides
    @TopAdsDashboardScope
    public ShopCommonWSApi provideShopCommonWsApi(@ShopWsQualifier Retrofit retrofit) {
        return retrofit.create(ShopCommonWSApi.class);
    }

    @Provides
    @TopAdsDashboardScope
    public ShopCommonCloudDataSource provideShopCommonCloudDataSource(ShopCommonApi shopCommonApi, ShopCommonWSApi shopCommonWS4Api,
                                                                      UserSession userSession) {
        return new ShopCommonCloudDataSource(shopCommonApi, shopCommonWS4Api, userSession);
    }

    @Provides
    @TopAdsDashboardScope
    public ShopCommonDataSource provideShopCommonDataSource(ShopCommonCloudDataSource shopInfoCloudDataSource) {
        return new ShopCommonDataSource(shopInfoCloudDataSource);
    }

    @Provides
    @TopAdsDashboardScope
    public ShopCommonRepository provideShopCommonRepository(ShopCommonDataSource shopInfoDataSource) {
        return new ShopCommonRepositoryImpl(shopInfoDataSource);
    }

    @Provides
    @TopAdsDashboardScope
    public GetShopInfoUseCase provideGetShopInfoUseCase(ShopCommonRepository shopCommonRepository) {
        return new GetShopInfoUseCase(shopCommonRepository);
    }

    @Provides
    @TopAdsDashboardScope
    public ShopDepositDataSource provideShopDepositDataSource(ShopDepositDataSourceCloud shopDepositDataSourceCloud){
        return new ShopDepositDataSource(shopDepositDataSourceCloud);
    }

    @Provides
    @TopAdsDashboardScope
    public ShopDepositDataSourceCloud provideShopDepositDataSourceCloud(TopAdsManagementApi topAdsManagementApi){
        return new ShopDepositDataSourceCloud(topAdsManagementApi);
    }

    @Provides
    @TopAdsDashboardScope
    public TopAdsShopDepositRepository provideTopAdsShopDepositRepository(ShopDepositDataSource shopDepositDataSource){
        return new TopAdsShopDepositRepositoryImpl(shopDepositDataSource);
    }

    @Provides
    @TopAdsDashboardScope
    public TopAdsCacheDataSource provideTopAdsCacheDataSource(@ApplicationContext Context context){
        return new TopAdsCacheDataSourceImpl(context);
    }

    @Provides
    @TopAdsDashboardScope
    public TopAdsDatePickerInteractor provideTopAdsDatePickerInteractor(TopAdsCacheDataSource topAdsCacheDataSource){
        return new TopAdsDatePickerInteractorImpl(topAdsCacheDataSource);
    }

    @Provides
    @TopAdsDashboardScope
    public TopAdsDashboardApi provideTopAdsDashboadrApi(@TopAdsDashboardQualifier Retrofit retrofit){
        return retrofit.create(TopAdsDashboardApi.class);
    }

    @Provides
    @TopAdsDashboardScope
    public TopAdsDashboardDataSourceCloud provideTopAdsDashboardDataSourceCloud(TopAdsDashboardApi topAdsDashboardApi){
        return new TopAdsDashboardDataSourceCloud(topAdsDashboardApi);
    }

    @Provides
    @TopAdsDashboardScope
    public TopAdsDashboardDataSource provideTopAdsDashboardDataSource(TopAdsDashboardDataSourceCloud topAdsDashboardDataSourceCloud){
        return new TopAdsDashboardDataSource(topAdsDashboardDataSourceCloud);
    }

    @Provides
    @TopAdsDashboardScope
    public TopAdsDashboardRepository provideTopAdsDashboardRepository(TopAdsDashboardDataSource topAdsDashboardDataSource){
        return new TopAdsDashboardRepositoryImpl(topAdsDashboardDataSource);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsSourceTaggingLocal provideTopAdsSourceTaggingLocal(@ApplicationContext Context context){
        return new TopAdsSourceTaggingLocal(context);
    }

    @TopAdsDashboardScope
    @Provides
    public TopAdsSourceTaggingDataSource provideTopAdsSourceTaggingDataSource(TopAdsSourceTaggingLocal topAdsSourceTaggingLocal){
        return new TopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal);
    }

    @TopAdsDashboardScope
    @Provides
    public TopAdsSourceTaggingRepository provideTopAdsSourceTaggingRepository(TopAdsSourceTaggingDataSource dataSource){
        return new TopAdsSourceTaggingRepositoryImpl(dataSource);
    }

    @TopAdsDashboardScope
    @Provides
    public DeleteTopAdsStatisticsUseCase provideDeleteTopAdsStatisticsUseCase(){
        return new DeleteTopAdsStatisticsUseCase();
    }

    @TopAdsDashboardScope
    @Provides
    public DeleteTopAdsTotalAdUseCase provideDeleteTopAdsTotalAdUseCase(){
        return new DeleteTopAdsTotalAdUseCase();
    }

    @TopAdsDashboardScope
    @Provides
    public GraphqlUseCase provideGraphqlUseCase(){
        return new GraphqlUseCase();
    }

    @Provides
    public TopAdsDashboardRouter provideTopAdsDashboardRouter(@ApplicationContext Context context){
        if (context instanceof TopAdsDashboardRouter){
            return (TopAdsDashboardRouter) context;
        }
        return null;
    }

    @Provides
    public TopAdsDashboardTracking provideTopAdsDashboardTracking(TopAdsDashboardRouter router){
        return new TopAdsDashboardTracking(router);
    }

}
