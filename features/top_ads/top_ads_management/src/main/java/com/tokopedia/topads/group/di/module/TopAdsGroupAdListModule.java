package com.tokopedia.topads.group.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.topads.common.data.source.local.TopAdsCacheDataSource;
import com.tokopedia.topads.common.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.topads.dashboard.di.qualifier.TopAdsManagementQualifier;
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractor;
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractorImpl;
import com.tokopedia.topads.group.data.apiservice.TopAdsGroupAdApi;
import com.tokopedia.topads.group.data.repository.TopAdsGroupAdRepositoryImpl;
import com.tokopedia.topads.group.data.source.TopAdsGroupAdDataSource;
import com.tokopedia.topads.group.data.source.cloud.TopAdsGroupAdDataSourceCloud;
import com.tokopedia.topads.group.di.scope.TopAdsGroupAdScope;
import com.tokopedia.topads.group.domain.repository.TopAdsGroupAdRepository;
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal;
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hadi.putra on 09/05/18.
 */
@TopAdsGroupAdScope
@Module
public class TopAdsGroupAdListModule {
    @Provides
    @TopAdsGroupAdScope
    public TopAdsGroupAdRepository provideTopAdsGroupAdRepository(TopAdsGroupAdDataSource dataSource){
        return new TopAdsGroupAdRepositoryImpl(dataSource);
    }

    @Provides
    @TopAdsGroupAdScope
    public TopAdsGroupAdDataSource provideTopAdsGroupAdDataSource(TopAdsGroupAdDataSourceCloud dataSourceCloud){
        return new TopAdsGroupAdDataSource(dataSourceCloud);
    }

    @Provides
    @TopAdsGroupAdScope
    public TopAdsGroupAdDataSourceCloud provideTopAdsGroupAdDataSourceCloud(TopAdsGroupAdApi TopAdsGroupAdApi){
        return new TopAdsGroupAdDataSourceCloud(TopAdsGroupAdApi);
    }

    @Provides
    @TopAdsGroupAdScope
    public TopAdsGroupAdApi provideTopAdsGroupAdApi(@TopAdsManagementQualifier Retrofit retrofit){
        return retrofit.create(TopAdsGroupAdApi.class);
    }

    @Provides
    @TopAdsGroupAdScope
    public TopAdsCacheDataSource provideTopAdsCacheDataSource(@ApplicationContext Context context){
        return new TopAdsCacheDataSourceImpl(context);
    }

    @Provides
    @TopAdsGroupAdScope
    public TopAdsDatePickerInteractor provideTopAdsDatePickerInteractor(TopAdsCacheDataSource topAdsCacheDataSource){
        return new TopAdsDatePickerInteractorImpl(topAdsCacheDataSource);
    }

    @Provides
    @TopAdsGroupAdScope
    TopAdsSourceTaggingLocal provideTopAdsSourceTaggingLocal(@ApplicationContext Context context){
        return new TopAdsSourceTaggingLocal(context);
    }

    @Provides
    @TopAdsGroupAdScope
    public TopAdsSourceTaggingDataSource provideTopAdsSourceTaggingDataSource(TopAdsSourceTaggingLocal topAdsSourceTaggingLocal){
        return new TopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal);
    }

    @Provides
    @TopAdsGroupAdScope
    public TopAdsSourceTaggingRepository provideTopAdsSourceTaggingRepository(TopAdsSourceTaggingDataSource dataSource){
        return new TopAdsSourceTaggingRepositoryImpl(dataSource);
    }
}
