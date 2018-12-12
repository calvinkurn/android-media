package com.tokopedia.topads.keyword.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.product.manage.item.common.data.mapper.SimpleDataResponseMapper;
//import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;
import com.tokopedia.product.manage.item.common.data.source.cloud.TomeProductApi;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepository;
import com.tokopedia.topads.common.data.source.local.TopAdsCacheDataSource;
import com.tokopedia.topads.common.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.topads.dashboard.di.qualifier.TopAdsManagementQualifier;
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractorImpl;
import com.tokopedia.topads.group.data.apiservice.TopAdsGroupAdApi;
import com.tokopedia.topads.group.data.repository.TopAdsGroupAdRepositoryImpl;
import com.tokopedia.topads.group.data.source.TopAdsGroupAdDataSource;
import com.tokopedia.topads.group.data.source.cloud.TopAdsGroupAdDataSourceCloud;
import com.tokopedia.topads.group.domain.repository.TopAdsGroupAdRepository;
import com.tokopedia.topads.keyword.data.repository.TopAdsKeywordRepositoryImpl;
import com.tokopedia.topads.keyword.data.repository.TopAdsOldKeywordRepositoryImpl;
import com.tokopedia.topads.keyword.data.source.TopAdsKeywordDataSource;
import com.tokopedia.topads.keyword.data.source.cloud.TopAdsKeywordDataSourceCloud;
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal;
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository;
import com.tokopedia.topads.keyword.data.source.KeywordDashboardDataSouce;
import com.tokopedia.topads.keyword.data.source.cloud.api.KeywordApi;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hendry on 5/18/2017.
 */

@TopAdsKeywordScope
@Module
public class TopAdsKeywordModule {

    @TopAdsKeywordScope
    @Provides
    KeywordApi provideKeywordApi(@TopAdsManagementQualifier Retrofit retrofit){
        return retrofit.create(KeywordApi.class);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordRepository provideTopAdsOldKeywordRepository(
            KeywordDashboardDataSouce keywordDashboardDataSouce,
            ShopInfoRepository shopInfoRepository) {
        return new TopAdsOldKeywordRepositoryImpl(keywordDashboardDataSouce, shopInfoRepository);
    }

    @TopAdsKeywordScope
    @Provides
    TomeProductApi provideTomeApi(@ShopQualifier Retrofit retrofit) {
        return retrofit.create(TomeProductApi.class);
    }

    @TopAdsKeywordScope
    @Provides
    SimpleDataResponseMapper<ShopModel> provideShopModelMapper() {
        return new SimpleDataResponseMapper<>();
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsSourceTaggingLocal provideTopAdsSourceTracking(@ApplicationContext Context context){
        return new TopAdsSourceTaggingLocal(context);
    }

    @TopAdsKeywordScope
    @Provides
    public TopAdsSourceTaggingDataSource provideTopAdsSourceTaggingDataSource(TopAdsSourceTaggingLocal topAdsSourceTaggingLocal){
        return new TopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal);
    }

    @TopAdsKeywordScope
    @Provides
    public TopAdsSourceTaggingRepository provideTopAdsSourceTaggingRepository(TopAdsSourceTaggingDataSource dataSource){
        return new TopAdsSourceTaggingRepositoryImpl(dataSource);
    }

    @TopAdsKeywordScope
    @Provides
    public TopAdsKeywordDataSourceCloud provideTopAdsKeywordDataSourceCloud(KeywordApi keywordApi){
        return new TopAdsKeywordDataSourceCloud(keywordApi);
    }

    @TopAdsKeywordScope
    @Provides
    public TopAdsKeywordDataSource provideTopAdsKeywordDataSource(TopAdsKeywordDataSourceCloud topAdsKeywordDataSourceCloud){
        return new TopAdsKeywordDataSource(topAdsKeywordDataSourceCloud);
    }

    @TopAdsKeywordScope
    @Provides
    public com.tokopedia.topads.keyword.domain.repository.TopAdsKeywordRepository provideTopAdsKeywordRepository(TopAdsKeywordDataSource dataSource){
        return new TopAdsKeywordRepositoryImpl(dataSource);
    }

    @TopAdsKeywordScope
    @Provides
    public TopAdsCacheDataSource provideTopAdsCacheDataSource(@ApplicationContext Context context){
        return new TopAdsCacheDataSourceImpl(context);
    }

    @TopAdsKeywordScope
    @Provides
    public TopAdsDatePickerInteractor provideTopAdsDatePickerInteractor(TopAdsCacheDataSource topAdsCacheDataSource){
        return new TopAdsDatePickerInteractorImpl(topAdsCacheDataSource);
    }

    @TopAdsKeywordScope
    @Provides
    public TopAdsGroupAdRepository provideTopAdsGroupAdRepository(TopAdsGroupAdDataSource dataSource){
        return new TopAdsGroupAdRepositoryImpl(dataSource);
    }

    @TopAdsKeywordScope
    @Provides
    public TopAdsGroupAdDataSource provideTopAdsGroupAdDataSource(TopAdsGroupAdDataSourceCloud dataSourceCloud){
        return new TopAdsGroupAdDataSource(dataSourceCloud);
    }

    @TopAdsKeywordScope
    @Provides
    public TopAdsGroupAdDataSourceCloud provideTopAdsGroupAdDataSourceCloud(TopAdsGroupAdApi TopAdsGroupAdApi){
        return new TopAdsGroupAdDataSourceCloud(TopAdsGroupAdApi);
    }

    @TopAdsKeywordScope
    @Provides
    public TopAdsGroupAdApi provideTopAdsGroupAdApi(@TopAdsManagementQualifier Retrofit retrofit){
        return retrofit.create(TopAdsGroupAdApi.class);
    }

}
