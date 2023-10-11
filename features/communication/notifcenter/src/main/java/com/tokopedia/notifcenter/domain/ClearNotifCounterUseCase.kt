package com.tokopedia.notifcenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.entity.clearnotif.ClearNotifCounterResponse
import com.tokopedia.notifcenter.data.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClearNotifCounterUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : FlowUseCase<Int, Resource<ClearNotifCounterResponse>>(dispatcher.io) {

    override fun graphqlQuery(): String = """
            mutation notifcenter_clearNotifCounter (
                $$PARAM_TYPE_ID: Int
            ){
              notifcenter_clearNotifCounter(
                    type_id: $$PARAM_TYPE_ID, 
                    form:"new"
                ) {
                data {
                  is_success
                }
              }
            }
    """.trimIndent()

    override suspend fun execute(params: Int): Flow<Resource<ClearNotifCounterResponse>> = flow {
        emit(Resource.loading(null))
        val param = generateParam(params)
        val networkFilter = repository.request<Map<*, *>, ClearNotifCounterResponse>(
            graphqlQuery(),
            param
        )
        emit(Resource.success(networkFilter))
    }

    private fun generateParam(
        @RoleType
        role: Int
    ): Map<String, Any?> {
        return mapOf(
            PARAM_TYPE_ID to role
        )
    }

    companion object {
        private const val PARAM_TYPE_ID = "type_id"
    }
}
