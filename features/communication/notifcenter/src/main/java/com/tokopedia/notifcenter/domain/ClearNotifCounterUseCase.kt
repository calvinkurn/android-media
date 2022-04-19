package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.entity.clearnotif.ClearNotifCounterResponse
import com.tokopedia.notifcenter.data.state.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClearNotifCounterUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ClearNotifCounterResponse>
) {

    fun clearNotifCounter(
            @RoleType
            role: Int
    ) = flow {
        emit(Resource.loading(null))
        val param = generateParam(role)
        val networkFilter = gqlUseCase.apply {
            setTypeClass(ClearNotifCounterResponse::class.java)
            setRequestParams(param)
            setGraphqlQuery(query)
        }.executeOnBackground()
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
        private val query = """
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
    }
}