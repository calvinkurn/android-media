package com.tokopedia.search.result.presentation.presenter;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.helper.GqlSearchQueryHelper;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.result.presentation.SearchContract;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class SearchPresenterModule {

    @SearchScope
    @Provides
    SearchContract.Presenter getSearchPresenter(GraphqlUseCase graphqlUseCase, GqlSearchQueryHelper gqlSearchQueryHelper) {
        return new SearchPresenter(graphqlUseCase, gqlSearchQueryHelper);
    }
}
