package com.tokopedia.search.result.domain.usecase.initiatesearch;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.R;
import com.tokopedia.search.result.data.mapper.initiatesearch.InitiateSearchMapperModule;
import com.tokopedia.usecase.UseCase;

import dagger.Module;
import dagger.Provides;
import rx.functions.Func1;

@SearchScope
@Module(includes = InitiateSearchMapperModule.class)
public class InitiateSearchUseCaseModule {

    @SearchScope
    @Provides
    UseCase<InitiateSearchModel> provideInitiateSearchUseCase(
            @ApplicationContext Context context,
            Func1<GraphqlResponse, InitiateSearchModel> initiateSearchModelMapper
    ) {

        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_initiate_search),
                InitiateSearchModel.class
        );

        return new InitiateSearchUseCase(graphqlRequest, new GraphqlUseCase(), initiateSearchModelMapper);
    }
}