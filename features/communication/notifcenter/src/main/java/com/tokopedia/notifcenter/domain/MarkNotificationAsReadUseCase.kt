package com.tokopedia.notifcenter.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.entity.markasread.MarkReadStatusResponse
import javax.inject.Inject

class MarkNotificationAsReadUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<MarkNotificationAsReadUseCase.Param, MarkReadStatusResponse>(
    dispatcher.io
) {

    override fun graphqlQuery(): String = """
            mutation notifcenter_markReadStatus (
                $$PARAM_NOTIF_ID: String!,
                $$PARAM_TYPE_ID: Int
            ){
              notifcenter_markReadStatus(
                    notif_id: $$PARAM_NOTIF_ID, 
                    type_id: $$PARAM_TYPE_ID, 
                    form: "new"
                ) {
                links{
                  self
                }
                status
                server_proccess_time
                server
                data{
                  is_success
                }
                message_error
              }
            }
    """.trimIndent()

    override suspend fun execute(params: Param): MarkReadStatusResponse {
        return repository.request(graphqlQuery(), params)
    }

    data class Param(
        @RoleType
        @SerializedName(PARAM_TYPE_ID)
        val role: Int,
        @SerializedName(PARAM_NOTIF_ID)
        val notifId: String
    ) : GqlParam

    companion object {
        private const val PARAM_NOTIF_ID = "notif_id"
        private const val PARAM_TYPE_ID = "type_id"
    }
}
