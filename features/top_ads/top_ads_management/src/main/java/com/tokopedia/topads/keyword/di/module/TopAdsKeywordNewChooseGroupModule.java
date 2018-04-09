package com.tokopedia.topads.keyword.di.module;

import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.topads.dashboard.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.topads.dashboard.data.repository.TopAdsGroupAdsRepositoryImpl;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSearchGroupAdsNameUseCase;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordNewChooseGroupPresenter;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordNewChooseGroupPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 5/24/17.
 */

@TopAdsKeywordScope
@Module
public class TopAdsKeywordNewChooseGroupModule extends TopAdsKeywordModule {

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordNewChooseGroupPresenter providePresenter(TopAdsSearchGroupAdsNameUseCase topAdsSearchGroupAdsNameUseCase){
        return new TopAdsKeywordNewChooseGroupPresenterImpl(topAdsSearchGroupAdsNameUseCase);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsGroupAdsRepository provideTopAdsGroupRepository(TopAdsGroupAdFactory topAdsGroupAdFactory){
        return new TopAdsGroupAdsRepositoryImpl(topAdsGroupAdFactory);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsManagementApi provideManagementApi(@TopAdsQualifier Retrofit retrofit){
        return retrofit.create(TopAdsManagementApi.class);
    }

}
