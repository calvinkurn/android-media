package com.tokopedia.search.result.domain.usecase.initiatesearch;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
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

class InitiateSearchUseCase extends UseCase<InitiateSearchModel> {

    private GraphqlRequest graphqlRequest;
    private GraphqlUseCase graphqlUseCase;
    private Func1<GraphqlResponse, InitiateSearchModel> initiateSearchModelMapper;

    InitiateSearchUseCase(GraphqlRequest graphqlRequest,
                                 GraphqlUseCase graphqlUseCase,
                                 Func1<GraphqlResponse, InitiateSearchModel> initiateSearchModelMapper) {

        this.graphqlRequest = graphqlRequest;
        this.graphqlUseCase = graphqlUseCase;
        this.initiateSearchModelMapper = initiateSearchModelMapper;
    }

    @Override
    public Observable<InitiateSearchModel> createObservable(RequestParams requestParams) {
        Map<String, Object> variables = createParametersForQuery(requestParams.getParameters());

        graphqlRequest.setVariables(variables);

        graphqlUseCase.addRequest(graphqlRequest);

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(initiateSearchModelMapper);
    }

    private Map<String, Object> createParametersForQuery(Map<String, Object> requestParams) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(KEY_PARAMS, UrlParamUtils.generateUrlParamString(requestParams));

        return variables;
    }
}