package com.tokopedia.exploreCategory.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.exploreCategory.model.ECDynamicHomeIconData
import com.tokopedia.exploreCategory.model.raw.GQL_EC_DYNAMIC_HOME_ICON_QUERY
import javax.inject.Inject

class ECRepository @Inject constructor() : BaseRepository() {

    private fun createRequestParamsForHomeIconData(): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_TYPE] = VALUE_TYPE
        return request
    }

    suspend fun getHomeIconData(): ECDynamicHomeIconData {
        return getGQLData(
                GQL_EC_DYNAMIC_HOME_ICON_QUERY,
                ECDynamicHomeIconData::class.java,
                createRequestParamsForHomeIconData()
        )
    }

    companion object {
        private const val PARAM_TYPE = "type"
        private const val VALUE_TYPE = 2
    }
}