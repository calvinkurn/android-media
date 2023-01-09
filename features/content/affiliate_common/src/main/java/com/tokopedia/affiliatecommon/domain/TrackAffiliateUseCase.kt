package com.tokopedia.affiliatecommon.domain

import android.text.TextUtils
import com.tokopedia.affiliatecommon.data.pojo.trackaffiliate.TrackAffiliateResponse
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by milhamj on 2019-08-20.
 */
class TrackAffiliateUseCase @Inject constructor(
    val graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<Boolean>() {

    var params: Map<String, Any> = mapOf()

    init {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )
    }

    override suspend fun executeOnBackground(): Boolean {
        val request = GraphqlRequest(QUERY.trimIndent(), TrackAffiliateResponse::class.java, params)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(request)
        val graphqlResponse: GraphqlResponse = graphqlUseCase.executeOnBackground()

        val error = graphqlResponse.getError(TrackAffiliateResponse::class.java)
        if (error != null && !TextUtils.isEmpty(error.firstOrNull()?.message)) {
            throw MessageErrorException(error.first().message)
        }

        val trackAffiliateResponse = graphqlResponse.getData<TrackAffiliateResponse>(TrackAffiliateResponse::class.java)
        return trackAffiliateResponse.topadsAffiliateTracker?.isSuccess ?: false
    }

    companion object {
        const val UNIQUE_STRING = "uniqueString"
        const val TRACKER_ID = "trackerID"

        private const val QUERY = """
            mutation AffiliateTracker(${'$'}trackerID: String!, ${'$'}uniqueString: String!) {
              topadsAffiliateTracker(input: {trackerID: ${'$'}trackerID, uniqueString: ${'$'}uniqueString}) {
                success
                message
                errors {
                  Code
                  Title
                  Detail
                }
              }
            }
        """

        fun createParams(uniqueString: String, deviceId: String): Map<String, Any> {
            return mutableMapOf<String, Any>().apply {
                put(UNIQUE_STRING, uniqueString)
                put(TRACKER_ID, deviceId)
            }
        }
    }
}
