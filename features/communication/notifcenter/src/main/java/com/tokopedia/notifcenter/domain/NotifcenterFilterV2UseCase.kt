package com.tokopedia.notifcenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.notifcenter.data.entity.filter.NotifcenterFilterResponse
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.util.cache.NotifCenterCacheManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class NotifcenterFilterV2UseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val cacheManager: NotifCenterCacheManager,
    dispatcher: CoroutineDispatchers
) : FlowUseCase<Int, Resource<NotifcenterFilterResponse>>(dispatcher.io) {

    override fun graphqlQuery(): String = """
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

    override suspend fun execute(params: Int): Flow<Resource<NotifcenterFilterResponse>> = flow {
        emit(Resource.loading(null))
        val cacheFilter = getFilterFromCache(params)
        if (cacheFilter != null) {
            emit(Resource.loading(cacheFilter))
        }
        val networkFilter = repository.request<Map<*, *>, NotifcenterFilterResponse>(
            graphqlQuery(),
            mapOf(
                PARAM_TYPE_ID to params
            )
        )
        val isNeedUpdate = isNeedUpdate(cacheFilter, networkFilter)
        if (isNeedUpdate) {
            saveToCache(params, networkFilter)
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
                cacheFilter.tagKey != networkFilter.tagKey
            ) {
                return true
            }
        }
        return false
    }

    companion object {
        private const val PARAM_TYPE_ID = "type_id"
    }
}
