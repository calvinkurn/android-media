package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.entity.markasread.MarkReadStatusResponse
import com.tokopedia.notifcenter.data.state.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MarkNotificationAsReadUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<MarkReadStatusResponse>
) {

    fun markAsRead(
            @RoleType
            role: Int,
            notifId: String
    ) = flow {
        emit(Resource.loading(null))
        val param = generateParam(role, notifId)
        val response = gqlUseCase.apply {
            setTypeClass(MarkReadStatusResponse::class.java)
            setRequestParams(param)
            setGraphqlQuery(query)
        }.executeOnBackground()
        emit(Resource.success(response))
    }

    private fun generateParam(
            @RoleType
            role: Int,
            notifId: String
    ): Map<String, Any?> {
        return mapOf(
                PARAM_TYPE_ID to role,
                PARAM_NOTIF_ID to notifId
        )
    }

    companion object {
        private const val PARAM_NOTIF_ID = "notif_id"
        private const val PARAM_TYPE_ID = "type_id"
        private val query = """
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
    }
}