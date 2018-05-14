package com.tokopedia.topads.product.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.topads.dashboard.data.source.local.TopAdsCacheDataSource;
import com.tokopedia.topads.dashboard.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.topads.dashboard.di.qualifier.TopAdsManagementQualifier;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsDatePickerInteractorImpl;
import com.tokopedia.topads.product.data.apiservice.TopAdsProductAdApi;
import com.tokopedia.topads.product.data.repository.TopAdsProductAdRepositoryImpl;
import com.tokopedia.topads.product.data.source.TopAdsProductAdDataSource;
import com.tokopedia.topads.product.data.source.cloud.TopAdsProductAdDataSourceCloud;
import com.tokopedia.topads.product.di.scope.TopAdsProductAdScope;
import com.tokopedia.topads.product.domain.repository.TopAdsProductAdRepository;
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal;
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hadi.putra on 04/05/18.
 */

@TopAdsProductAdScope
@Module
public class TopAdsProductAdListModule {

    @Provides
    @TopAdsProductAdScope
    public TopAdsProductAdRepository provideTopAdsProductAdRepository(TopAdsProductAdDataSource dataSource){
        return new TopAdsProductAdRepositoryImpl(dataSource);
    }

    @Provides
    @TopAdsProductAdScope
    public TopAdsProductAdDataSource provideTopAdsProductAdDataSource(TopAdsProductAdDataSourceCloud dataSourceCloud){
        return new TopAdsProductAdDataSource(dataSourceCloud);
    }

    @Provides
    @TopAdsProductAdScope
    public TopAdsProductAdDataSourceCloud provideTopAdsProductAdDataSourceCloud(TopAdsProductAdApi topAdsProductAdApi){
        return new TopAdsProductAdDataSourceCloud(topAdsProductAdApi);
    }

    @Provides
    @TopAdsProductAdScope
    public TopAdsProductAdApi productAdApiTopAdsProductAdApi(@TopAdsManagementQualifier Retrofit retrofit){
        return retrofit.create(TopAdsProductAdApi.class);
    }

    @Provides
    @TopAdsProductAdScope
    public TopAdsCacheDataSource provideTopAdsCacheDataSource(@ApplicationContext Context context){
        return new TopAdsCacheDataSourceImpl(context);
    }

    @Provides
    @TopAdsProductAdScope
    public TopAdsDatePickerInteractor provideTopAdsDatePickerInteractor(TopAdsCacheDataSource topAdsCacheDataSource){
        return new TopAdsDatePickerInteractorImpl(topAdsCacheDataSource);
    }

    @Provides
    @TopAdsProductAdScope
    TopAdsSourceTaggingLocal provideTopAdsSourceTaggingLocal(@ApplicationContext Context context){
        return new TopAdsSourceTaggingLocal(context);
    }

    @Provides
    @TopAdsProductAdScope
    public TopAdsSourceTaggingDataSource provideTopAdsSourceTaggingDataSource(TopAdsSourceTaggingLocal topAdsSourceTaggingLocal){
        return new TopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal);
    }

    @Provides
    @TopAdsProductAdScope
    public TopAdsSourceTaggingRepository provideTopAdsSourceTaggingRepository(TopAdsSourceTaggingDataSource dataSource){
        return new TopAdsSourceTaggingRepositoryImpl(dataSource);
    }
}
