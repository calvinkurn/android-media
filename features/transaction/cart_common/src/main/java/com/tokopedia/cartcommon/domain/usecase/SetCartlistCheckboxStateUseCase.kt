package com.tokopedia.cartcommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cartcommon.data.request.checkbox.SetCartlistCheckboxStateRequest
import com.tokopedia.cartcommon.data.response.checkbox.SetCartlistCheckboxGqlResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class SetCartlistCheckboxStateUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<List<SetCartlistCheckboxStateRequest>, Boolean>(dispatcher.io) {

    companion object {
        private const val PARAM = "params"

        const val QUERY_SET_CARTLIST_CHECKBOX_STATE = "SetCartlistCheckboxStateQuery"

        const val SET_CHECKBOX_STATE_QUERY = """
    mutation setCheckboxState(${'$'}params: [CartCheckboxStateParam]) {
      set_cartlist_checkbox_state(params: ${'$'}params) {
        status
        error_message
        data {
          success
        }
      }
    }
"""
    }

    override fun graphqlQuery(): String = SET_CHECKBOX_STATE_QUERY

    @GqlQuery(QUERY_SET_CARTLIST_CHECKBOX_STATE, SET_CHECKBOX_STATE_QUERY)
    override suspend fun execute(params: List<SetCartlistCheckboxStateRequest>): Boolean {
        val param = mapOf(PARAM to params)
        val response =
            graphqlRepository.request<Map<String, Any>, SetCartlistCheckboxGqlResponse>(
                SetCartlistCheckboxStateQuery(),
                param
            )
        return response.setCartlistCheckboxStateResponse.data.success == 1
    }
}
