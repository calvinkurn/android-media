package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifcenter.data.entity.bumpreminder.BumpReminderResponse
import com.tokopedia.notifcenter.data.state.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotifcenterSetReminderBumpUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<BumpReminderResponse>
) {

    fun bumpReminder(
            productId: String,
            notifId: String
    ) = flow {
        emit(Resource.loading(null))
        val param = generateParam(productId, notifId)
        val response = gqlUseCase.apply {
            setTypeClass(BumpReminderResponse::class.java)
            setRequestParams(param)
            setGraphqlQuery(query)
        }.executeOnBackground()
        emit(Resource.success(response))
    }

    private fun generateParam(
            productId: String, notifId: String
    ): Map<String, Any?> {
        return mapOf(
                PARAM_ID to productId,
                PARAM_NOTIF_ID to notifId
        )
    }

    companion object {
        private const val PARAM_ID = "id"
        private const val PARAM_NOTIF_ID = "notif_id"
        private val query = """
            mutation notifcenter_setReminderBump(
                $$PARAM_ID: String!,
                $$PARAM_NOTIF_ID: String!
            ){
              notifcenter_setReminderBump(
                    id: $$PARAM_ID, 
                    notif_id: $$PARAM_NOTIF_ID
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