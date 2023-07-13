package com.tokopedia.notifcenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.util.cache.NotifCenterCacheManager
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotifOrderListUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val cacheManager: NotifCenterCacheManager,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : FlowUseCase<Int, Resource<NotifOrderListResponse>>(dispatchers.io) {

    override fun graphqlQuery(): String = """
        query notifcenter_notifOrderList(
            $$PARAM_TYPE_USER: Int
        ) {
          notifcenter_notifOrderList(
            type_of_user: $$PARAM_TYPE_USER
          ) {
            list {
              icon
              text
              counter_str
              link {
                android_link
              }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(
        @RoleType params: Int
    ): Flow<Resource<NotifOrderListResponse>> = flow {
        emit(Resource.loading(null))
        val param = generateParam(params)
        val cache = getOrderListFromCache(params)
        cache?.let {
            it.clearAllCounter()
            emit(Resource.loading(it))
        }
        val response = repository.request<Map<*, *>, NotifOrderListResponse>(graphqlQuery(), param)
        emit(Resource.success(response))
        saveOrderListToCache(params, response)
    }

    private fun getOrderListFromCache(userType: Int): NotifOrderListResponse? {
        val cacheKey = getCacheKey(userType)
        return cacheManager.loadCache(cacheKey, NotifOrderListResponse::class.java)
    }

    private fun saveOrderListToCache(userType: Int, response: NotifOrderListResponse) {
        val cacheKey = getCacheKey(userType)
        return cacheManager.saveCache(cacheKey, response)
    }

    private fun getCacheKey(userType: Int): String {
        return "notif_order_list_$userType-${userSession.userId}"
    }

    private fun generateParam(
        @RoleType
        userType: Int
    ): Map<String, Any?> {
        return mapOf(
            PARAM_TYPE_USER to userType
        )
    }

    companion object {
        const val PARAM_TYPE_USER = "type_of_user"
    }
}
