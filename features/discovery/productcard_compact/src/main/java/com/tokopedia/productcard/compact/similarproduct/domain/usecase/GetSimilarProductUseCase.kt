package com.tokopedia.productcard.compact.similarproduct.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toUrlParams
import com.tokopedia.productcard.compact.similarproduct.domain.model.ProductRecommendationResponse
import com.tokopedia.productcard.compact.similarproduct.domain.query.ProductRecommendationQuery
import com.tokopedia.productcard.compact.similarproduct.domain.query.ProductRecommendationQuery.PARAM_PRODUCT_IDS
import com.tokopedia.productcard.compact.similarproduct.domain.query.ProductRecommendationQuery.PARAM_QUERY
import com.tokopedia.productcard.compact.similarproduct.domain.query.ProductRecommendationQuery.PARAM_USER_ID
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Get Product Recommendation Widget Single GQL Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/PSM/pages/545653484/Recommendation+Widget+Single+GraphQL+for+single+component
 */
class GetSimilarProductUseCase @Inject constructor(gqlRepository: GraphqlRepository) {

    companion object {
        private const val DEFAULT_USER_ID = 0
        private const val DEFAULT_PRODUCT_IDS = ""
    }

    private val graphql by lazy { GraphqlUseCase<ProductRecommendationResponse>(gqlRepository) }

    /**
     * @param userId userid of the user
     * @param productIds comma separated product ids in a string
     */
    suspend fun execute(
        userId: Int = DEFAULT_USER_ID,
        productIds: String = DEFAULT_PRODUCT_IDS,
        queryParam: MutableMap<String, Any>
    ): ProductRecommendationResponse {
        graphql.apply {
            setGraphqlQuery(ProductRecommendationQuery)
            setTypeClass(ProductRecommendationResponse::class.java)

            setRequestParams(RequestParams.create().apply {
                putInt(PARAM_USER_ID, userId)
                putString(PARAM_PRODUCT_IDS, productIds)
                putString(PARAM_QUERY, queryParam.toUrlParams())
            }.parameters)

            return executeOnBackground()
        }
    }
}
