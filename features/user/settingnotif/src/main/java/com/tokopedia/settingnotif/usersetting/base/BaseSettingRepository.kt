package com.tokopedia.settingnotif.usersetting.base

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse

/**
 * this class is created to preventing unmockable to graphRepository.
 * the reason is, graphqlRepository has been made into a compiled-class
 * that cannot be mocked when unit-test.
 */
class BaseSettingRepository(
        private val repository: GraphqlRepository
): SettingRepository {

    override suspend fun getResponse(
            requests: List<GraphqlRequest>,
            cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        return repository.getReseponse(requests, cacheStrategy)
    }

}