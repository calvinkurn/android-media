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
        const val PARAM_INBOX_ID = "inboxID"
    }

    private val query by lazy {
        """
            query getProductRevIncentiveOvo(${'$'}inboxID: String) {
              productrevIncentiveOvo(inboxID: ${'$'}inboxID) {
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

    suspend fun getIncentiveOvo(inboxReviewId: String = ""): ProductRevIncentiveOvoDomain {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val graphqlRequest = if (inboxReviewId.isNotBlank()) {
            val requestParams = RequestParams.create().apply {
                putString(PARAM_INBOX_ID, inboxReviewId)
            }.parameters
            GraphqlRequest(query, ProductRevIncentiveOvoDomain::class.java, requestParams)
        } else {
            GraphqlRequest(query, ProductRevIncentiveOvoDomain::class.java)
        }
        val response = graphqlRepository.getReseponse(listOf(graphqlRequest), cacheStrategy)
        val data: ProductRevIncentiveOvoDomain? = response.getData(ProductRevIncentiveOvoDomain::class.java)
        if (data == null) {
            throw RuntimeException()
        } else {
            return data
        }
    }

}