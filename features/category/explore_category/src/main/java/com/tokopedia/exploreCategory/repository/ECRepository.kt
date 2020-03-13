package com.tokopedia.exploreCategory.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.exploreCategory.ECConstants
import com.tokopedia.exploreCategory.model.ECDynamicHomeIconData
import javax.inject.Inject
import javax.inject.Named

class ECRepository @Inject constructor(
        @Named(ECConstants.gql_ec_dynamic_home_icon_query)
        val query: String) : BaseRepository() {

    private fun createRequestParamsForHomeIconData(): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_TYPE] = VALUE_TYPE
        return request
    }

    suspend fun getHomeIconData(): ECDynamicHomeIconData {
        return getGQLData(
                query,
                ECDynamicHomeIconData::class.java,
                createRequestParamsForHomeIconData()
        )
    }

    companion object {
        private const val PARAM_TYPE = "type"
        private const val VALUE_TYPE = 2
    }
}