package com.tokopedia.exploreCategory.usecase

import com.tokopedia.exploreCategory.ECConstants.Companion.gql_ec_dynamic_home_icon_query
import com.tokopedia.exploreCategory.model.ECDynamicHomeIconData
import com.tokopedia.exploreCategory.repository.ECRepository
import javax.inject.Inject
import javax.inject.Named

class ECDynamicHomeIconUseCase @Inject constructor(
        @Named(gql_ec_dynamic_home_icon_query)
        val query: String,
        private val repository: ECRepository) {

    private fun createRequestParams(): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_TYPE] = VALUE_TYPE
        return request
    }

    suspend fun getHomeIconData(): ECDynamicHomeIconData {
        return repository.getGQLData(
                query,
                ECDynamicHomeIconData::class.java,
                createRequestParams()
        )
    }

    companion object {
        private val PARAM_TYPE = "type"
        private val VALUE_TYPE = 2
    }
}