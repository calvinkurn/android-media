package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.entity.notification.Paging
import com.tokopedia.notifcenter.data.mapper.NotifcenterDetailMapper
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class NotifcenterDetailUseCase @Inject constructor(
        @Named(QUERY_NOTIFCENTER_DETAIL_V3)
        private val query: String,
        private val gqlUseCase: GraphqlUseCase<NotifcenterDetailResponse>,
        private val mapper: NotifcenterDetailMapper,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope by MainScope() {

    var timeZone = TimeZone.getDefault().id
    var pagingNew = Paging()
    var pagingEarlier = Paging()

    fun getFirstPageNotification(
            filter: Long,
            @RoleType
            role: Int,
            onSuccess: (NotificationDetailResponseModel) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        val fields = if (!hasFilter(filter)) {
            arrayOf("new")
        } else {
            emptyArray()
        }
        val params = generateParam(
                filter, role, "", fields
        )
        val needSectionTitle = !hasFilter(filter)
        val needLoadMoreButton = needSectionTitle
        getNotifications(
                params, onSuccess, onError,
                { response ->
                    mapper.mapFirstPage(response, needSectionTitle, needLoadMoreButton)
                },
                { response ->
                    updateNewPaging(response)
                    updateEarlierPaging(response)
                }
        )
    }

    fun getMoreNewNotifications(
            filter: Long,
            @RoleType
            role: Int,
            onSuccess: (NotificationDetailResponseModel) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        val params = generateParam(
                filter, role, pagingNew.lastNotifId, arrayOf("new")
        )
        val needLoadMoreButton = !hasFilter(filter)
        getNotifications(
                params, onSuccess, onError,
                { response ->
                    mapper.mapNewSection(response, false, needLoadMoreButton)
                },
                { response ->
                    updateNewPaging(response)
                }
        )
    }

    fun getMoreEarlierNotifications(
            filter: Long,
            @RoleType
            role: Int,
            onSuccess: (NotificationDetailResponseModel) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        val params = generateParam(
                filter, role, pagingEarlier.lastNotifId, emptyArray()
        )
        val needLoadMoreButton = !hasFilter(filter)
        getNotifications(
                params, onSuccess, onError,
                { response ->
                    mapper.mapEarlierSection(response, false, needLoadMoreButton)
                },
                { response ->
                    updateEarlierPaging(response)
                }
        )
    }

    private fun hasFilter(filter: Long): Boolean {
        return filter != FILTER_NONE
    }

    private fun getNotifications(
            params: Map<String, Any?>,
            onSuccess: (NotificationDetailResponseModel) -> Unit,
            onError: (Throwable) -> Unit,
            mapping: (response: NotifcenterDetailResponse) -> NotificationDetailResponseModel,
            onResponseReady: (response: NotifcenterDetailResponse) -> Unit
    ) {
        launchCatchError(
                dispatchers.io,
                {
                    val response = gqlUseCase.apply {
                        setTypeClass(NotifcenterDetailResponse::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    val items = mapping(response)
                    withContext(dispatchers.main) {
                        onResponseReady(response)
                        onSuccess(items)
                    }
                },
                {
                    withContext(dispatchers.main) {
                        onError(it)
                    }
                }
        )
    }

    private fun updateNewPaging(response: NotifcenterDetailResponse) {
        pagingNew = response.notifcenterDetail.newPaging
    }

    private fun updateEarlierPaging(response: NotifcenterDetailResponse) {
        pagingEarlier = response.notifcenterDetail.paging
    }

    private fun generateParam(
            filter: Long,
            @RoleType
            role: Int,
            lastNotifId: String,
            fields: Array<String>
    ): Map<String, Any?> {
        return mapOf(
                PARAM_TYPE_ID to role,
                PARAM_TAG_ID to filter,
                PARAM_TIMEZONE to timeZone,
                PARAM_LAST_NOTIF_ID to lastNotifId,
                PARAM_FIELDS to fields
        )
    }

    fun cancelRunningOperation() {
        coroutineContext.cancelChildren()
    }

    companion object {
        const val QUERY_NOTIFCENTER_DETAIL_V3 = "QUERY_NOTIFCENTER_DETAIL_V3"
        const val FILTER_NONE: Long = 0
        val queryRes = R.raw.query_notifcenter_detail_v3

        private const val PARAM_TYPE_ID = "type_id"
        private const val PARAM_TAG_ID = "tag_id"
        private const val PARAM_TIMEZONE = "timezone"
        private const val PARAM_LAST_NOTIF_ID = "last_notif_id"
        private const val PARAM_FIELDS = "fields"
    }

}