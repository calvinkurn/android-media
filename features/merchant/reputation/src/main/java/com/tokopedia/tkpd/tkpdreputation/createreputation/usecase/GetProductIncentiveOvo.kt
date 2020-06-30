package com.tokopedia.tkpd.tkpdreputation.createreputation.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ProductRevIncentiveOvo
import javax.inject.Inject
import javax.inject.Named

class GetProductIncentiveOvo @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                 @Named("review_incentive_ovo") private val rawQuery: String) {

    suspend fun getIncentiveOvo(): ProductRevIncentiveOvo {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val graphqlRequest = GraphqlRequest(rawQuery, ProductRevIncentiveOvo::class.java)

        val response = graphqlRepository.getReseponse(listOf(graphqlRequest), cacheStrategy)

        val data: ProductRevIncentiveOvo? = response.getData(ProductRevIncentiveOvo::class.java)
        if (data == null) {
            throw RuntimeException()
        } else {
            return data
        }
    }

}