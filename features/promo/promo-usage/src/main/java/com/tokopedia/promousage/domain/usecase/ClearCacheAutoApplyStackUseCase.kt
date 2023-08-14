package com.tokopedia.promousage.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.clearpromo.ClearCacheAutoApplyStackResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@GqlQuery(
    ClearCacheAutoApplyStackUseCase.QUERY_NAME,
    ClearCacheAutoApplyStackUseCase.QUERY
)
class ClearCacheAutoApplyStackUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository
) : CoroutineUseCase<ClearPromoRequest, ClearCacheAutoApplyStackResponse>(
    Dispatchers.IO
) {

    override suspend fun execute(params: ClearPromoRequest): ClearCacheAutoApplyStackResponse {
        return repository.request(QUERY.trimIndent(), params)
    }

    override fun graphqlQuery(): String = QUERY.trimIndent()

    companion object {
        const val PARAM_VALUE_MARKETPLACE = "marketplace"

        const val QUERY_NAME = "ClearCacheAutoApplyStackUseCase"
        const val QUERY = """
            mutation clearCacheAutoApplyStack(${"$"}serviceID: String!, ${"$"}promoCode: [String], ${"$"}isOCC: Boolean, ${"$"}orderData: OrderDataInput) {
                clearCacheAutoApplyStack(serviceID: ${"$"}serviceID, promoCode: ${"$"}promoCode, isOCC: ${"$"}isOCC, orderData: ${"$"}orderData) {
                    Success
                    ticker_message
                    default_empty_promo_message
                }
            }
        """
    }
}
