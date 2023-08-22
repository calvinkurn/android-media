package com.tokopedia.promousage.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.promousage.data.request.GetPromoListRecommendationParam
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@GqlQuery(
    GetPromoListRecommendationEntryPointUseCase.QUERY_NAME,
    GetPromoListRecommendationEntryPointUseCase.QUERY
)
class GetPromoListRecommendationEntryPointUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository
) : CoroutineUseCase<GetPromoListRecommendationParam, GetPromoListRecommendationResponse>(
    Dispatchers.IO
) {

    override suspend fun execute(params: GetPromoListRecommendationParam): GetPromoListRecommendationResponse {
        return repository.request(GetPromoListRecommendationUseCase.QUERY.trimIndent(), params)
    }

    override fun graphqlQuery(): String = QUERY.trimIndent()

    companion object {
        const val QUERY_NAME: String = "GetPromoListRecommendationEntryPointQuery"
        const val QUERY: String = """
            mutation GetPromoListRecommendation(${'$'}params: PromoDisplayPromoStackRequest, ${'$'}chosen_address: PromoDisplayChosenAddressParam, ${'$'}is_promo_revamp: Boolean) {
              GetPromoListRecommendation(params: ${'$'}params, chosen_address: ${'$'}chosen_address, is_promo_revamp: ${'$'}is_promo_revamp) {
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
        """
    }
}
