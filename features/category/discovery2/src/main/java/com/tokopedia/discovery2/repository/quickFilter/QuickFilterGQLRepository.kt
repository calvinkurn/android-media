package com.tokopedia.discovery2.repository.quickFilter

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME

const val ADD_FILTERS_COUNT = true

open class QuickFilterGQLRepository : BaseRepository(), IQuickFilterGqlRepository {
    override suspend fun getQuickFilterProductCountData(componentId: String,
                                                        pageEndPoint: String,
                                                        selectedFilterMapParameter: Map<String, String>,
                                                        userId: String?): DiscoveryResponse {
        return (getGQLData(GQL_COMPONENT,
                DataResponse::class.java, Utils.getQueryMap(componentId, pageEndPoint,
                selectedFilterMapParameter, userId, ADD_FILTERS_COUNT), GQL_COMPONENT_QUERY_NAME)
                as DataResponse).data
    }
}
