package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.data.state.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class NotifOrderListUseCase @Inject constructor(
        @Named(QUERY_ORDER_LIST)
        private val query: String,
        private val gqlUseCase: GraphqlUseCase<NotifOrderListResponse>
) {

    fun getOrderList(
            @RoleType
            userType: Int
    ) = flow {
        emit(Resource.loading(null))
        val param = generateParam(userType)
        val response = gqlUseCase.apply {
            setTypeClass(NotifOrderListResponse::class.java)
            setRequestParams(param)
            setGraphqlQuery(query)
        }.executeOnBackground()
        emit(Resource.success(response))
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