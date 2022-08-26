package com.tokopedia.tokofood.feature.search.srp.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.common.domain.param.TokoFoodMerchantListParamMapper
import com.tokopedia.tokofood.feature.search.srp.domain.response.TokofoodSearchMerchantResponse
import javax.inject.Inject

private const val QUERY = """
        query TokofoodSearchMerchant(${'$'}params: String!, ${'$'}pageKey: String, ${'$'}limit: Int) {
          tokofoodSearchMerchant(params: ${'$'}params, pageKey: ${'$'}pageKey, limit: ${'$'}limit) {
            filter {
              title
              subtitle
              options {
                name
                key
                value
                inputType
                isNew
              }
            }
            sort {
              name
              key
              value
              inputType
              applyFilter
            }
          }
        }
    """


@GqlQuery("TokofoodSearchMerchant", QUERY)
class TokofoodSearchMerchantUseCase @Inject constructor(
    repository: GraphqlRepository
) : GraphqlUseCase<TokofoodSearchMerchantResponse>(repository) {

    init {
        setTypeClass(TokofoodSearchMerchantResponse::class.java)
        setGraphqlQuery(TokofoodSearchMerchant())
    }

    suspend fun execute(
        localCacheModel: LocalCacheModel?,
        option: Int = 0,
        brandUId: String = "",
        sortBy: Int = 0,
        orderById: Int = 0,
        cuisine: String = "",
        pageKey: String = ""
    ): TokofoodSearchMerchantResponse {
        setRequestParams(
            TokoFoodMerchantListParamMapper.createRequestParams(
                localCacheModel, option,
                brandUId, sortBy, orderById, cuisine, pageKey
            )
        )
        return executeOnBackground()
    }

}