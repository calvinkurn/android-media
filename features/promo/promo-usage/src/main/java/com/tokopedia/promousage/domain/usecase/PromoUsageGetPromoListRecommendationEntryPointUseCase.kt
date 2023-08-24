package com.tokopedia.promousage.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.promousage.data.request.GetPromoListRecommendationParam
import com.tokopedia.promousage.data.response.GetPromoListRecommendationEntryPointResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@GqlQuery(
    PromoUsageGetPromoListRecommendationEntryPointUseCase.QUERY_NAME,
    PromoUsageGetPromoListRecommendationEntryPointUseCase.QUERY
)
class PromoUsageGetPromoListRecommendationEntryPointUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository
) : CoroutineUseCase<GetPromoListRecommendationParam, GetPromoListRecommendationEntryPointResponse>(
    Dispatchers.IO
) {

    override suspend fun execute(params: GetPromoListRecommendationParam): GetPromoListRecommendationEntryPointResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = QUERY.trimIndent()

    companion object {
        const val QUERY_NAME: String = "PromoUsageGetPromoListRecommendationEntryPointQuery"
        const val QUERY: String = """
            mutation getPromoListRecommendationEntryPoint(${'$'}params: GetPromoListRecomendationRequest, ${'$'}chosen_address: PromoDisplayChosenAddressParam, ${'$'}is_promo_revamp: Boolean) {
              GetPromoListRecommendation(params: ${'$'}params, chosen_address: ${'$'}chosen_address, is_promo_revamp: ${'$'}is_promo_revamp) {
                data {
                  result_status {
                    success
                    message
                    code
                  }
                  entry_point_info {
                    messages
                    state
                    icon_url
                    clickable
                  }
                  user_group_metadata
                }
              }
            }
        """
    }
}
