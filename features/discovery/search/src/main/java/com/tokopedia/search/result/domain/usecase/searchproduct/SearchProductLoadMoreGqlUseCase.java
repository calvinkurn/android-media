package com.tokopedia.search.result.domain.usecase.searchproduct;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;

class SearchProductLoadMoreGqlUseCase extends UseCase<SearchProductModel> {

    private GraphqlRequest graphqlRequest;
    private GraphqlUseCase graphqlUseCase;
    private Func1<GraphqlResponse, SearchProductModel> searchProductModelMapper;

    SearchProductLoadMoreGqlUseCase(GraphqlRequest graphqlRequest,
                                    GraphqlUseCase graphqlUseCase,
                                    Func1<GraphqlResponse, SearchProductModel> searchProductModelMapper) {
        this.graphqlRequest = graphqlRequest;
        this.graphqlUseCase = graphqlUseCase;
        this.searchProductModelMapper = searchProductModelMapper;
    }

    @Override
    public Observable<SearchProductModel> createObservable(RequestParams requestParams) {
        Map<String, Object> variables = createParametersForQuery(requestParams.getParameters());

        graphqlRequest.setVariables(variables);

        graphqlUseCase.addRequest(graphqlRequest);

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(searchProductModelMapper);
    }

    private Map<String, Object> createParametersForQuery(Map<String, Object> parameters) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(KEY_PARAMS, UrlParamUtils.generateUrlParamString(parameters));

        return variables;
    }
}
