package com.tokopedia.search.result.domain.usecase.getdynamicfilter

import rx.functions.Func1
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.discovery.common.constants.SearchConstant.GQL
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.data.response.GqlDynamicFilterResponse
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.HashMap

internal class GetDynamicFilterGqlUseCase(
        private val graphqlUseCase: GraphqlUseCase,
        private val dynamicFilterModelGqlMapper: Func1<GraphqlResponse?, DynamicFilterModel?>
) : UseCase<DynamicFilterModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<DynamicFilterModel> {
        val variables = createParametersForQuery(requestParams.parameters)

        val graphqlRequest = createGraphqlRequest()

        graphqlRequest.variables = variables
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(dynamicFilterModelGqlMapper)
    }

    private fun createGraphqlRequest(): GraphqlRequest {
        return GraphqlRequest(
                GQL_QUERY,
                GqlDynamicFilterResponse::class.java
        )
    }

    private fun createParametersForQuery(parameters: Map<String?, Any?>): Map<String, Any> {
        val variables: MutableMap<String, Any> = HashMap()
        variables[GQL.KEY_PARAMS] = UrlParamUtils.generateUrlParamString(parameters)
        return variables
    }

    companion object {
        private const val GQL_QUERY = "query SearchProduct(\n" +
                " \$params: String!\n" +
                ") {\n" +
                "filter_sort_product(params: \$params) {\n" +
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
                "}"
    }
}