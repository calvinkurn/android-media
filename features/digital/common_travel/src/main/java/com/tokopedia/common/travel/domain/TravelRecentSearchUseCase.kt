package com.tokopedia.common.travel.domain

import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

/**
 * @author by furqan on 14/01/2020
 */

class TravelRecentSearchUseCase @Inject constructor(private val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(query: String, isFromCloud: Boolean): TravelRecentSearchModel {
        val cacheType = if (isFromCloud) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(cacheType).build())
        useCase.clearRequest()

        try {
            val graphqlRequest = GraphqlRequest(query, TravelRecentSearchModel.Response::class.java)
            useCase.addRequest(graphqlRequest)

            return useCase.executeOnBackground().getSuccessData<TravelRecentSearchModel.Response>().response
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

}