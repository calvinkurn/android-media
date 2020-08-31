package com.tokopedia.salam.umrah.travel.presentation.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.salam.umrah.travel.data.UmrahTravelAgentBySlugNameEntity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by Firman on 22/1/20
 */

class UmrahTravelUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<UmrahTravelAgentBySlugNameEntity>(graphqlRepository) {

    suspend fun executeUsecase(rawQuery: String, slugName: String): Result<UmrahTravelAgentBySlugNameEntity> {
        return try {
            val params = mapOf(UMRAH_TRAVEL_AGENT_SLUG to slugName)
            this.setGraphqlQuery(rawQuery)
            this.setRequestParams(params)
            this.setTypeClass(UmrahTravelAgentBySlugNameEntity::class.java)
            this.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            val umrahTravelAgent = this.executeOnBackground()
            Success(umrahTravelAgent)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    companion object {
        const val UMRAH_TRAVEL_AGENT_SLUG = "slugName"
    }
}