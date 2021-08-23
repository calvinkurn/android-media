package com.tokopedia.review.feature.ovoincentive.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
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
        const val OVO_INCENTIVE_QUERY_CLASS_NAME = "OvoIncentive"
        const val OVO_INCENTIVE_QUERY = """
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
                    bottom_sheet
                  }
                }
            """
    }

    @GqlQuery(OVO_INCENTIVE_QUERY_CLASS_NAME, OVO_INCENTIVE_QUERY)
    suspend fun getIncentiveOvo(productId: String = "", reputationId: String = ""): ProductRevIncentiveOvoDomain? {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val graphqlRequest = if (productId.isNotBlank() && reputationId.isNotBlank()) {
            val requestParams = RequestParams.create().apply {
                putString(PARAM_PRODUCT_ID, productId)
                putString(PARAM_REPUTATION_ID, reputationId)
            }.parameters
            GraphqlRequest(OvoIncentive.GQL_QUERY, ProductRevIncentiveOvoDomain::class.java, requestParams)
        } else {
            GraphqlRequest(OvoIncentive.GQL_QUERY, ProductRevIncentiveOvoDomain::class.java)
        }
        val response = graphqlRepository.getReseponse(listOf(graphqlRequest), cacheStrategy)
        return response.getData(ProductRevIncentiveOvoDomain::class.java)
    }

}