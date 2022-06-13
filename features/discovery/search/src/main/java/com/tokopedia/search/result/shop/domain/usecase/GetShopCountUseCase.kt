package com.tokopedia.search.result.shop.domain.usecase

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.coroutines.UseCase
import timber.log.Timber

internal class GetShopCountUseCase(
        private val graphqlUseCase: GraphqlUseCase<GetShopCountModel>
): UseCase<Int>() {

    companion object {
        private const val QUERY = """
            query SearchShop(${'$'}params: String!) { 
                aceSearchShop(params: ${'$'}params) { 
                    total_shop 
                } 
            }
        """
    }

    @GqlQuery("GetShopCount", QUERY)
    override suspend fun executeOnBackground(): Int {
        graphqlUseCase.setRequestParams(createRequestParams())
        graphqlUseCase.setTypeClass(GetShopCountModel::class.java)
        graphqlUseCase.setGraphqlQuery(GetShopCount.GQL_QUERY)

        return try {
            graphqlUseCase.executeOnBackground().searchShopCount.countText
        }
        catch (exception: Throwable) {
            Timber.w(exception)
            0
        }
    }

    private fun createRequestParams(): Map<String, Any> {
        val variables = mutableMapOf<String, Any>()

        variables[SearchConstant.GQL.KEY_PARAMS] = UrlParamUtils.generateUrlParamString(useCaseRequestParams.parameters)

        return variables
    }

    internal class GetShopCountModel {
        @SerializedName("aceSearchShop")
        @Expose
        val searchShopCount = AceSearchShop()

        class AceSearchShop {
            @SerializedName("total_shop")
            @Expose
            val countText = 0
        }
    }
}