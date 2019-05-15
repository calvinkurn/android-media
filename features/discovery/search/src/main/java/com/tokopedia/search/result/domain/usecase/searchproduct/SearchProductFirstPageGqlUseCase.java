package com.tokopedia.search.result.domain.usecase.searchproduct;

import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_HEADLINE_PARAMS;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_QUERY;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.HEADLINE;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.HEADLINE_ITEM_VALUE;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.HEADLINE_TEMPLATE_VALUE;

class SearchProductFirstPageGqlUseCase extends UseCase<SearchProductModel> {

    private GraphqlRequest graphqlRequest;
    private GraphqlUseCase graphqlUseCase;
    private Func1<GraphqlResponse, SearchProductModel> searchProductModelMapper;

    SearchProductFirstPageGqlUseCase(GraphqlRequest graphqlRequest,
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

        variables.put(KEY_QUERY, getQueryFromParameters(parameters));
        variables.put(KEY_PARAMS, UrlParamUtils.generateUrlParamString(parameters));
        variables.put(KEY_HEADLINE_PARAMS, createHeadlineParams(parameters));

        return variables;
    }

    private Object getQueryFromParameters(Map<String, Object> parameters) {
        Object query = parameters.get(SearchApiConst.Q);

        return query == null ? "" : query;
    }

    private String createHeadlineParams(Map<String, Object> parameters) {
        Map<String, Object> headlineParams = new HashMap<>(parameters);
        headlineParams.put(TopAdsParams.KEY_EP, HEADLINE);
        headlineParams.put(TopAdsParams.KEY_TEMPLATE_ID, HEADLINE_TEMPLATE_VALUE);
        headlineParams.put(TopAdsParams.KEY_ITEM, HEADLINE_ITEM_VALUE);

        return UrlParamUtils.generateUrlParamString(headlineParams);
    }
}
