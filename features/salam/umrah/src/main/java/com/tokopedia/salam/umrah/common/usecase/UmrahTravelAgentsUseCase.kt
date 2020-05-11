package com.tokopedia.salam.umrah.common.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsInput
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by firman on 20/01/19
 */

class UmrahTravelAgentsUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<UmrahTravelAgentsEntity>(graphqlRepository) {

    suspend fun executeUseCase(rawQuery: String, fromCloud: Boolean = false, page: Int = 1,
                               limit: Int = 20, flags: List<String>): Result<UmrahTravelAgentsEntity> {
        try {
            val umrahTravelAgentsParams = UmrahTravelAgentsInput(page, limit, flags)

            val params = mapOf(PARAM_UMRAH_TRAVEL_AGENTS to umrahTravelAgentsParams)

            this.setGraphqlQuery(rawQuery)
            this.setRequestParams(params)
            this.setTypeClass(UmrahTravelAgentsEntity::class.java)
            this.setCacheStrategy(GraphqlCacheStrategy.Builder(if (fromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            val umrahTravelAgents = this.executeOnBackground()
            return Success(umrahTravelAgents)
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }

    companion object {
        private const val PARAM_UMRAH_TRAVEL_AGENTS = "params"
    }
}