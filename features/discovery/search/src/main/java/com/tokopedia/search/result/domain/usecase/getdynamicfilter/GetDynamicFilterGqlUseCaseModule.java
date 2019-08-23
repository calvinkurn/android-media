package com.tokopedia.search.result.domain.usecase.getdynamicfilter;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.R;
import com.tokopedia.search.result.data.response.GqlDynamicFilterResponse;
import com.tokopedia.search.result.data.mapper.dynamicfilter.DynamicFilterGqlMapperModule;
import com.tokopedia.usecase.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.functions.Func1;

@Module(includes = DynamicFilterGqlMapperModule.class)
public class GetDynamicFilterGqlUseCaseModule {

    @Provides
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
    UseCase<DynamicFilterModel> provideGetDynamicFilterGqlUseCase(
            @ApplicationContext Context context,
            Func1<GraphqlResponse, DynamicFilterModel> dynamicFilterModelMapper
    ) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_search_filter_product),
                GqlDynamicFilterResponse.class
        );

        return new GetDynamicFilterGqlUseCase(graphqlRequest, new GraphqlUseCase(), dynamicFilterModelMapper);
    }
}
