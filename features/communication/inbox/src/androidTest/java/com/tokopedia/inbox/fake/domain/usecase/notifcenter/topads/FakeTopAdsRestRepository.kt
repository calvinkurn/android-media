package com.tokopedia.inbox.fake.domain.usecase.notifcenter.topads

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.sdk.domain.model.TopAdsBannerResponse
import java.lang.reflect.Type

class FakeTopAdsRestRepository : GraphqlRepository {

    var isError = false
    var response = TopAdsBannerResponse(
        TopAdsBannerResponse.TopadsDisplayBannerAdsV3(null, null)
    )

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        if (isError) {
            throw IllegalStateException("Error Get TDN")
        }
        val map = mutableMapOf<Type, Any>(
            TopAdsBannerResponse::class.java to response
        )
        return GraphqlResponse(
            map,
            mutableMapOf(),
            false
        )
    }
}
