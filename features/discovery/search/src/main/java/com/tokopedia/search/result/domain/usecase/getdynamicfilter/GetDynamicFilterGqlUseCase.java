package com.tokopedia.search.result.domain.usecase.getdynamicfilter;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_QUERY;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_SOURCE;

class GetDynamicFilterGqlUseCase extends UseCase<DynamicFilterModel> {

    private GraphqlRequest graphqlRequest;
    private GraphqlUseCase graphqlUseCase;
    private Func1<GraphqlResponse, DynamicFilterModel> dynamicFilterModelGqlMapper;

    GetDynamicFilterGqlUseCase(GraphqlRequest graphqlRequest,
                               GraphqlUseCase graphqlUseCase,
                               Func1<GraphqlResponse, DynamicFilterModel> dynamicFilterModelGqlMapper) {
        this.graphqlRequest = graphqlRequest;
        this.graphqlUseCase = graphqlUseCase;
        this.dynamicFilterModelGqlMapper = dynamicFilterModelGqlMapper;
    }

    @Override
    public Observable<DynamicFilterModel> createObservable(RequestParams requestParams) {
        Map<String, Object> variables = createParametersForQuery(requestParams.getParameters());

        graphqlRequest.setVariables(variables);

        graphqlUseCase.addRequest(graphqlRequest);

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(dynamicFilterModelGqlMapper);
    }

    private Map<String, Object> createParametersForQuery(Map<String, Object> parameters) {
        Map<String, Object> variables = new HashMap<>();

        variables.put(KEY_QUERY, getQueryFromParameters(parameters));
        variables.put(KEY_PARAMS, UrlParamUtils.generateUrlParamString(parameters));
        variables.put(KEY_SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_PRODUCT);

        return variables;
    }

    private Object getQueryFromParameters(Map<String, Object> parameters) {
        Object query = parameters.get(SearchApiConst.Q);

        return query == null ? "" : query;
    }
}
