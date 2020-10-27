package com.tokopedia.review.feature.createreputation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetProductIncentiveOvo @Inject constructor(private val graphqlRepository: GraphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_REPUTATION_ID = "reputationID"
    }

    private val query by lazy {
        """
            query getProductRevIncentiveOvo(${'$'}productID: String, ${'$'}reputationID: String) {
              productrevIncentiveOvo(productID: ${'$'}productID, reputationID: ${'$'}reputationID) {
                ticker {
                  title
                  subtitle
                }
                title
                subtitle
                description
                numbered_list
                cta_text
                amount
              }
            }
        """.trimIndent()
    }

    suspend fun getIncentiveOvo(productId: Int = 0, reputationId: Int = 0): ProductRevIncentiveOvoDomain? {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val graphqlRequest = if (productId != 0 && reputationId != 0) {
            val requestParams = RequestParams.create().apply {
                putString(PARAM_PRODUCT_ID, productId.toString())
                putString(PARAM_REPUTATION_ID, reputationId.toString())
            }.parameters
            GraphqlRequest(query, ProductRevIncentiveOvoDomain::class.java, requestParams)
        } else {
            GraphqlRequest(query, ProductRevIncentiveOvoDomain::class.java)
        }
        val response = graphqlRepository.getReseponse(listOf(graphqlRequest), cacheStrategy)
        return response.getData(ProductRevIncentiveOvoDomain::class.java)
    }

}