package com.tokopedia.promousage.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.promousage.data.request.GetCouponListRecommendationParam
import com.tokopedia.promousage.data.response.GetCouponListRecommendationResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@GqlQuery(
    GetCouponListRecommendationEntryPointUseCase.QUERY_NAME,
    GetCouponListRecommendationEntryPointUseCase.QUERY
)
class GetCouponListRecommendationEntryPointUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository
) : CoroutineUseCase<GetCouponListRecommendationParam, GetCouponListRecommendationResponse>(
    Dispatchers.IO
) {

    override suspend fun execute(params: GetCouponListRecommendationParam): GetCouponListRecommendationResponse {
        return repository.request(GetCouponListRecommendationUseCase.QUERY.trimIndent(), params)
    }

    override fun graphqlQuery(): String = QUERY.trimIndent()

    companion object {
        const val QUERY_NAME: String = "GetCouponListRecommendationEntryPointUseCase"
        const val QUERY: String = """
            mutation coupon_list_recommendation(${'$'}params: PromoStackRequest, ${'$'}chosen_address: ChosenAddressParam) {
                coupon_list_recommendation(params: ${'$'}params, chosen_address: ${'$'}chosen_address) {
                    message
                    error_code
                    status
                    data {
                        result_status {
                            code
                            message
                            reason
                        }
                    }
                }
            }
        """
    }
}
