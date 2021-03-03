package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class NotifOrderListUseCase @Inject constructor(
        @Named(QUERY_ORDER_LIST)
        private val query: String,
        private val gqlUseCase: GraphqlUseCase<NotifOrderListResponse>,
        private val cacheManager: NotifcenterCacheManager,
        private val userSession: UserSessionInterface
) {

    fun getOrderList(
            @RoleType
            userType: Int
    ) = flow {
        emit(Resource.loading(null))
        val param = generateParam(userType)
        val cache = getOrderListFromCache(userType)
        cache?.let {
            it.clearAllCounter()
            emit(Resource.loading(it))
        }
        val response = gqlUseCase.apply {
            setTypeClass(NotifOrderListResponse::class.java)
            setRequestParams(param)
            setGraphqlQuery(query)
        }.executeOnBackground()
        emit(Resource.success(response))
        saveOrderListToCache(userType, response)
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
        const val QUERY_ORDER_LIST = "notif_order_list"
        const val PARAM_TYPE_USER = "type_of_user"
        val queryRes = R.raw.query_notifcenter_order_list
    }
}