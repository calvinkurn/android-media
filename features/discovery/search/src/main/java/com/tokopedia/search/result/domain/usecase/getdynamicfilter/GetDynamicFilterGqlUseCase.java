package com.tokopedia.search.result.domain.usecase.getdynamicfilter;

import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.result.data.response.GqlDynamicFilterResponse;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;

class GetDynamicFilterGqlUseCase extends UseCase<DynamicFilterModel> {

    private GraphqlRequest graphqlRequest;
    private GraphqlUseCase graphqlUseCase;
    private Func1<GraphqlResponse, DynamicFilterModel> dynamicFilterModelGqlMapper;

    GetDynamicFilterGqlUseCase(GraphqlUseCase graphqlUseCase,
                               Func1<GraphqlResponse, DynamicFilterModel> dynamicFilterModelGqlMapper) {
        graphqlRequest = new GraphqlRequest(
                GQL_QUERY,
                GqlDynamicFilterResponse.class
        );
        this.graphqlUseCase = graphqlUseCase;
        this.dynamicFilterModelGqlMapper = dynamicFilterModelGqlMapper;
    }

    @Override
    public Observable<DynamicFilterModel> createObservable(RequestParams requestParams) {
        Map<String, Object> variables = createParametersForQuery(requestParams.getParameters());

        graphqlRequest.setVariables(variables);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(dynamicFilterModelGqlMapper);
    }

    private Map<String, Object> createParametersForQuery(Map<String, Object> parameters) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(KEY_PARAMS, UrlParamUtils.generateUrlParamString(parameters));
        return variables;
    }

    private static final String GQL_QUERY = "query SearchProduct(\n" +
            " $params: String!\n" +
            ") {\n" +
            "filter_sort_product(params: $params) {\n" +
            "      data {\n" +
            "        filter {\n" +
            "          title\n" +
            "          search {\n" +
            "            searchable\n" +
            "            placeholder\n" +
            "          }\n" +
            "      template_name\n" +
            "          options {\n" +
            "            name\n" +
            "            key\n" +
            "            icon\n" +
            "            Description\n" +
            "            value\n" +
            "            inputType\n" +
            "            totalData\n" +
            "            valMax\n" +
            "            valMin\n" +
            "            isPopular\n" +
            "            isNew\n" +
            "            hexColor\n" +
            "            child {\n" +
            "              key\n" +
            "              value\n" +
            "              name\n" +
            "              icon\n" +
            "              inputType\n" +
            "              totalData\n" +
            "              isPopular\n" +
            "              child {\n" +
            "                key\n" +
            "                value\n" +
            "                name\n" +
            "                icon\n" +
            "                inputType\n" +
            "                totalData\n" +
            "                isPopular\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "        sort {\n" +
            "          name\n" +
            "          key\n" +
            "          value\n" +
            "          inputType\n" +
            "          applyFilter\n" +
            "        }\n" +
            "     }\n" +
            "  }\n" +
            "}";
}
