package com.tokopedia.topads.keyword.di.module;

import com.tokopedia.topads.keyword.data.repository.TopAdsKeywordDeleteRepositoryImpl;
import com.tokopedia.topads.keyword.data.repository.TopAdsKeywordGetDetailRepositoryImpl;
import com.tokopedia.topads.keyword.data.source.TopAdsKeywordActionBulkDataSource;
import com.tokopedia.topads.keyword.data.source.TopAdsKeywordGetDetailDataSource;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordActionBulkRepository;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordGetDetailRepository;
import com.tokopedia.topads.keyword.domain.interactor.TopAdsOldKeywordActionBulkUseCase;
import com.tokopedia.topads.keyword.domain.interactor.TopAdsKeywordGetDetailUseCase;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordDetailPresenterImpl;
import com.tokopedia.topads.keyword.view.presenter.TopadsKeywordDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

@TopAdsKeywordScope
@Module
public class TopAdsKeywordDetailModule extends TopAdsKeywordModule {

    @TopAdsKeywordScope
    @Provides
    TopadsKeywordDetailPresenter providePresenterKeywordDetail(TopAdsKeywordGetDetailUseCase topAdsKeywordGetDetailUseCase,
                                                               TopAdsOldKeywordActionBulkUseCase topAdsOldKeywordActionBulkUseCase){
        return new TopAdsKeywordDetailPresenterImpl(topAdsKeywordGetDetailUseCase, topAdsOldKeywordActionBulkUseCase);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordGetDetailRepository provideTopAdsDetailRepository(TopAdsKeywordGetDetailDataSource topAdsKeywordGetDetailDataSource){
        return new TopAdsKeywordGetDetailRepositoryImpl(topAdsKeywordGetDetailDataSource);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordActionBulkRepository provideTopAdsKeywordDetailRepository(TopAdsKeywordActionBulkDataSource topAdsKeywordActionBulkDataSource){
        return new TopAdsKeywordDeleteRepositoryImpl(topAdsKeywordActionBulkDataSource);
    }
}
