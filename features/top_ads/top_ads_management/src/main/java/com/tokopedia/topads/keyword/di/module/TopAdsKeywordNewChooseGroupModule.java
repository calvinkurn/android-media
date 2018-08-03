package com.tokopedia.topads.keyword.di.module;

import com.tokopedia.topads.dashboard.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.topads.dashboard.data.repository.TopAdsGroupAdsRepositoryImpl;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsOldManagementApi;
import com.tokopedia.topads.dashboard.di.qualifier.TopAdsManagementQualifier;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSearchGroupAdsNameUseCase;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordOldNewChooseGroupPresenter;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordOldNewChooseGroupPresenterImpl;

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
    TopAdsKeywordOldNewChooseGroupPresenter providePresenter(TopAdsSearchGroupAdsNameUseCase topAdsSearchGroupAdsNameUseCase){
        return new TopAdsKeywordOldNewChooseGroupPresenterImpl(topAdsSearchGroupAdsNameUseCase);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsGroupAdsRepository provideTopAdsGroupRepository(TopAdsGroupAdFactory topAdsGroupAdFactory){
        return new TopAdsGroupAdsRepositoryImpl(topAdsGroupAdFactory);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsOldManagementApi provideManagementApi(@TopAdsManagementQualifier Retrofit retrofit){
        return retrofit.create(TopAdsOldManagementApi.class);
    }

}
