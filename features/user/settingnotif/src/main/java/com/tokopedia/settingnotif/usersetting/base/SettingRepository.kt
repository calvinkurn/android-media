package com.tokopedia.settingnotif.usersetting.base

import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse

interface SettingRepository {

    suspend fun getResponse(
            requests: List<GraphqlRequest>,
            cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse

}