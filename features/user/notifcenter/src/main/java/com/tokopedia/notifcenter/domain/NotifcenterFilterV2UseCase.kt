package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import com.tokopedia.notifcenter.data.entity.filter.NotifcenterFilterResponse
import com.tokopedia.notifcenter.data.state.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotifcenterFilterV2UseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<NotifcenterFilterResponse>,
        private val cacheManager: NotifcenterCacheManager
) {

    fun getFilter(
            @RoleType
            role: Int
    ) = flow {
        emit(Resource.loading(null))
        val cacheFilter = getFilterFromCache(role)
        if (cacheFilter != null) {
            emit(Resource.loading(cacheFilter))
        }
        val networkFilter = getFilterFromNetwork(role)
        val isNeedUpdate = isNeedUpdate(cacheFilter, networkFilter)
        if (isNeedUpdate) {
            saveToCache(role, networkFilter)
        }
        emit(Resource.success(networkFilter, isNeedUpdate))
    }

    private fun getFilterFromCache(role: Int): NotifcenterFilterResponse? {
        val cacheKey = getCacheKey(role)
        return cacheManager.loadCache(cacheKey, NotifcenterFilterResponse::class.java)
    }

    private fun saveToCache(role: Int, networkFilter: NotifcenterFilterResponse) {
        val cacheKey = getCacheKey(role)
        cacheManager.saveCache(cacheKey, networkFilter)
    }

    private fun getCacheKey(role: Int): String {
        return "$role - ${javaClass.name}"
    }

    private suspend fun getFilterFromNetwork(
            @RoleType
            role: Int
    ): NotifcenterFilterResponse {
        val param = generateParam(role)
        return gqlUseCase.apply {
            setTypeClass(NotifcenterFilterResponse::class.java)
            setRequestParams(param)
            setGraphqlQuery(query)
        }.executeOnBackground()
    }

    private fun generateParam(
            @RoleType
            role: Int
    ): Map<String, Any> {
        return mapOf(
                PARAM_TYPE_ID to role
        )
    }

    private fun isNeedUpdate(
            cacheFilterResponse: NotifcenterFilterResponse?,
            networkFilterResponse: NotifcenterFilterResponse
    ): Boolean {
        if (cacheFilterResponse == null) return true
        val cacheFilters = cacheFilterResponse.notifcenterFilterV2.notifcenterTagList.list
        val networkFilters = networkFilterResponse.notifcenterFilterV2.notifcenterTagList.list
        if (cacheFilters.size != networkFilters.size) return true
        val cacheFiltersMap = cacheFilters.associateBy { it.tagId }
        for (networkFilter in networkFilters) {
            val cacheFilter = cacheFiltersMap[networkFilter.tagId] ?: return true
            if (cacheFilter.tagName != networkFilter.tagName ||
                    cacheFilter.tagKey != networkFilter.tagKey) {
                return true
            }
        }
        return false
    }

    companion object {
        private const val PARAM_TYPE_ID = "type_id"
        private val query = """
            query notifcenter_filter_v2 ($$PARAM_TYPE_ID: Int) {
              notifcenter_filter_v2(type_id: $$PARAM_TYPE_ID) {
                notifcenter_tagList{
                  list{
                    tag_id
                    tag_key
                    tag_name
                    status
                    create_by
                    create_time_unix
                    update_by
                    update_time_unix
                  }
                }
              }
            }
        """.trimIndent()
    }
}