package com.tokopedia.review.feature.createreputation.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import javax.inject.Inject

class GetProductIncentiveOvo @Inject constructor(private val graphqlRepository: GraphqlRepository) {

    companion object {
        const val OVO_INCENTIVE_QUERY_CLASS_NAME = "OvoIncentive"
        const val OVO_INCENTIVE_QUERY = """
                query getProductRevIncentiveOvo{
                    productrevIncentiveOvo{
                    ticker{
                      title
                      subtitle
                    }
                    title
                    subtitle
                    description
                    numbered_list
                    cta_text
                  }
                }
            """
    }

    @GqlQuery(OVO_INCENTIVE_QUERY_CLASS_NAME, OVO_INCENTIVE_QUERY)
    suspend fun getIncentiveOvo(): ProductRevIncentiveOvoDomain {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val graphqlRequest = GraphqlRequest(OvoIncentive.GQL_QUERY, ProductRevIncentiveOvoDomain::class.java)

        val response = graphqlRepository.getReseponse(listOf(graphqlRequest), cacheStrategy)

        val data: ProductRevIncentiveOvoDomain? = response.getData(ProductRevIncentiveOvoDomain::class.java)
        if (data == null) {
            throw RuntimeException()
        } else {
            return data
        }
    }

}