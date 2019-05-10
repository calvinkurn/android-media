package com.tokopedia.search.result.presentation.presenter;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.result.presentation.SearchContract;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class SearchPresenterModule {

    @SearchScope
    @Provides
    SearchContract.Presenter getSearchPresenter(GraphqlUseCase graphqlUseCase,
                                                @Named(SearchConstant.GQL_INITIATE_SEARCH)GqlSpecification gqlInitiateSearchSpec) {
        return new SearchPresenter(graphqlUseCase, gqlInitiateSearchSpec);
    }
}
