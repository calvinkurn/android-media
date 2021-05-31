package com.tokopedia.tokomart.searchcategory.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokomart.searchcategory.domain.model.GetProductCountModel
import com.tokopedia.usecase.coroutines.UseCase

class GetProductCountUseCase(
        private val graphqlUseCase: GraphqlUseCase<GetProductCountModel>
): UseCase<String>() {

    override suspend fun executeOnBackground(): String {
        graphqlUseCase.setTypeClass(GetProductCountModel::class.java)
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(
                mapOf(SearchConstant.GQL.KEY_PARAMS to UrlParamUtils.generateUrlParamString(useCaseRequestParams.parameters))
        )

        return graphqlUseCase.executeOnBackground().searchProductCount.countText
    }

    companion object {
        private const val QUERY =
                "query SearchProduct(\$params: String!) {\n" +
                "   searchProduct(params:\$params) {\n" +
                "       count_text\n" +
                "    }\n" +
                "}"
    }
}