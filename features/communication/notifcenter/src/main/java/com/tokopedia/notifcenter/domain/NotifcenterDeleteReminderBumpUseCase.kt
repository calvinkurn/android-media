package com.tokopedia.notifcenter.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.notifcenter.data.entity.deletereminder.DeleteReminderResponse
import com.tokopedia.notifcenter.data.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotifcenterDeleteReminderBumpUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : FlowUseCase<NotifcenterDeleteReminderBumpUseCase.Param, Resource<DeleteReminderResponse>>(
    dispatcher.io
) {
    override fun graphqlQuery(): String = """
            mutation notifcenter_deleteReminderBump(
                $$PARAM_ID: String!,
                $$PARAM_NOTIF_ID: String!
            ){
              notifcenter_deleteReminderBump(
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

    override suspend fun execute(params: Param): Flow<Resource<DeleteReminderResponse>> = flow {
        emit(Resource.loading(null))
        val response = repository.request<Param, DeleteReminderResponse>(graphqlQuery(), params)
        emit(Resource.success(response))
    }

    data class Param(
        @SerializedName(PARAM_ID)
        val productId: String,
        @SerializedName(PARAM_NOTIF_ID)
        val notifId: String
    ) : GqlParam

    companion object {
        private const val PARAM_ID = "id"
        private const val PARAM_NOTIF_ID = "notif_id"
    }
}
