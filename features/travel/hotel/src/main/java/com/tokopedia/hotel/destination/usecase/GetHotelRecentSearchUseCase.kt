package com.tokopedia.hotel.destination.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.common.util.QueryHotelGetRecentSearch
import com.tokopedia.hotel.destination.data.model.RecentSearch
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by jessica on 03/11/20
 */

class GetHotelRecentSearchUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
): UseCase<List<RecentSearch>>() {

    var params = mapOf<String, Any>()
    override suspend fun executeOnBackground(): List<RecentSearch> {
        val gqlRequest = GraphqlRequest(QueryHotelGetRecentSearch(), RecentSearch.Response::class.java, params)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(RecentSearch.Response::class.java)
        if (!errors.isNullOrEmpty()){
            throw  MessageErrorException(errors[0].message)
        } else {
            return gqlResponse.getData<RecentSearch.Response>(RecentSearch.Response::class.java).recentSearch
        }
    }
}