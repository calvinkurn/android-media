package com.tokopedia.hotel.destination.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.destination.HotelDestinationQueries
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by jessica on 03/11/20
 */

class GetPropertyPopularUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
): UseCase<List<PopularSearch>>() {

    override suspend fun executeOnBackground(): List<PopularSearch> {
        val gqlRequest = GraphqlRequest(HotelDestinationQueries.GET_POPULAR_PROPERTY_QUERY, PopularSearch.Response::class.java)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`()).build())
        val errors = gqlResponse.getError(PopularSearch.Response::class.java)
        if (!errors.isNullOrEmpty()){
            throw  MessageErrorException(errors[0].message)
        } else {
            return gqlResponse.getData<PopularSearch.Response>(PopularSearch.Response::class.java).popularSearchList
        }
    }
}