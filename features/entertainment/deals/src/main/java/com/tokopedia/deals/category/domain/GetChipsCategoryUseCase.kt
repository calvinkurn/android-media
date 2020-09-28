package com.tokopedia.deals.category.domain

import com.tokopedia.deals.common.domain.DealsGqlQueries
import com.tokopedia.deals.search.model.response.CuratedData
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetChipsCategoryUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
): UseCase<CuratedData>() {

    override suspend fun executeOnBackground(): CuratedData {
        val gqlRequest = GraphqlRequest(DealsGqlQueries.getChildCategory(), CuratedData::class.java)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`()).build())
        val errors = gqlResponse.getError(CuratedData::class.java)
        if (!errors.isNullOrEmpty()){
            throw  MessageErrorException(errors[0].message)
        } else {
            return gqlResponse.getData(CuratedData::class.java)
        }
    }
}