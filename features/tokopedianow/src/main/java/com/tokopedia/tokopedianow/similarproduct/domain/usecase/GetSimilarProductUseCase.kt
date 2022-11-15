package com.tokopedia.tokopedianow.similarproduct.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.similarproduct.domain.model.ProductRecommendationResponse
import com.tokopedia.tokopedianow.similarproduct.domain.query.SimilarProductQuery
import com.tokopedia.tokopedianow.similarproduct.domain.query.SimilarProductQuery.PARAM_PRODUCT_IDS
import com.tokopedia.tokopedianow.similarproduct.domain.query.SimilarProductQuery.PARAM_USER_ID
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Get Recipe GQL Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/1973782733/GQL+Get+Recipe
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
    ): ProductRecommendationResponse {
        graphql.apply {
            setGraphqlQuery(SimilarProductQuery)
            setTypeClass(ProductRecommendationResponse::class.java)

            setRequestParams(RequestParams.create().apply {
                putInt(PARAM_USER_ID, userId)
                putString(PARAM_PRODUCT_IDS, productIds)
            }.parameters)

            return executeOnBackground()
        }
    }
}
