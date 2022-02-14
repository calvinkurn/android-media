package com.tokopedia.discovery2.repository.section

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.config.GlobalConfig
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_SECTION
import com.tokopedia.discovery2.data.gqlraw.SECTION_QUERY_NAME

class SectionGQLRepository : BaseRepository(),SectionRepository {
    override suspend fun getComponents(
        pageIdentifier: String,
        sectionId: String,
        filterQueryString:String
    ): List<ComponentsItem> {
        val dataResponse = (getGQLData(GQL_SECTION,
            DataResponse::class.java, getQueryMap(pageIdentifier, sectionId,filterQueryString),
            SECTION_QUERY_NAME) as DataResponse).data
        return dataResponse.components
    }

    private fun getQueryMap(pageIdentifier: String,
                            sectionId: String,
                            queryString: String): Map<String,Any> {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap[Utils.IDENTIFIER] = pageIdentifier
        queryParameterMap[Utils.DEVICE] = Utils.DEVICE_VALUE
        queryParameterMap[Utils.SECTION_ID] = sectionId
        queryParameterMap[Utils.VERSION] = GlobalConfig.VERSION_NAME
        if (queryString.isNotEmpty()) queryParameterMap[Utils.FILTERS] = queryString
        return queryParameterMap
    }
}