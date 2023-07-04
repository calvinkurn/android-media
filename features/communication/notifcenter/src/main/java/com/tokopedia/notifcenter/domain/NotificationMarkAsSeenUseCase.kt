package com.tokopedia.notifcenter.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.entity.markasseen.MarkAsSeenResponse
import javax.inject.Inject

class NotificationMarkAsSeenUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<NotificationMarkAsSeenUseCase.Param, Unit>(dispatchers.io) {

    override fun graphqlQuery(): String = """
        mutation notifcenter_markSeenNotif($$PARAM_TYPE_ID: Int!, $$PARAM_NOTIF_ID: [String]!){
          notifcenter_markSeenNotif(type_id:$$PARAM_TYPE_ID, notif_id:$$PARAM_NOTIF_ID, track_id:"main-android") {
            links {
              self
            }
            status
            server_proccess_time
            server
            data {
              is_success
            }
            message_error
          }
        }
    """.trimIndent()

    override suspend fun execute(params: Param) {
        repository.request<Param, MarkAsSeenResponse>(graphqlQuery(), params)
    }

    data class Param(
        @SerializedName(PARAM_TYPE_ID)
        @RoleType
        val role: Int,
        @SerializedName(PARAM_NOTIF_ID)
        val notifIds: List<String>
    ) : GqlParam

    companion object {
        const val PARAM_TYPE_ID = "type_id"
        const val PARAM_NOTIF_ID = "notif_id"
    }
}
