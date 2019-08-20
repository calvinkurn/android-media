package com.tokopedia.affiliatecommon.domain

import com.tokopedia.affiliatecommon.data.pojo.trackaffiliate.TrackAffiliateResponse
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.UseCase

/**
 * @author by milhamj on 2019-08-20.
 */
class TrackAffiliateUseCase(val query: String,
                            val graphqlUseCase: MultiRequestGraphqlUseCase): UseCase<Boolean>() {

    var params: Map<String, Any> = mapOf()

    init {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )
    }

    override suspend fun executeOnBackground(): Boolean {
        val request = GraphqlRequest(query, TrackAffiliateResponse::class.java, params)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(request)
        val graphqlResponse: GraphqlResponse = graphqlUseCase.executeOnBackground()
        val trackAffiliateResponse = graphqlResponse.getData<TrackAffiliateResponse>(TrackAffiliateResponse::class.java)
        return trackAffiliateResponse.topadsAffiliateTracker?.isSuccess ?: false
    }

    companion object {
        const val UNIQUE_STRING = "uniqueString"
        const val TRACKER_ID = "trackerID"

        fun createParams(uniqueString: String, deviceId: String): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                put(UNIQUE_STRING, uniqueString)
                put(TRACKER_ID, deviceId)
            }
        }
    }
}