package com.tokopedia.cartrevamp.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cartrevamp.data.model.request.SetCartlistCheckboxStateRequest
import com.tokopedia.cartrevamp.data.model.response.cartlistcheckboxstate.SetCartlistCheckboxGqlResponse
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class SetCartlistCheckboxStateUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<List<CartItemHolderData>, Boolean>(dispatcher.io) {

    companion object {
        private const val PARAM = "params"

        const val QUERY_SET_CARTLIST_CHECKBOX_STATE = "SetCartlistCheckboxStateQuery"
    }

    override fun graphqlQuery(): String = SET_CHECKBOX_STATE_QUERY

    @GqlQuery(QUERY_SET_CARTLIST_CHECKBOX_STATE, SET_CHECKBOX_STATE_QUERY)
    override suspend fun execute(params: List<CartItemHolderData>): Boolean {
        val cartlistCheckboxStateRequestList = ArrayList<SetCartlistCheckboxStateRequest>()
        params.forEach {
            cartlistCheckboxStateRequestList.add(
                SetCartlistCheckboxStateRequest(
                    cartId = it.cartId,
                    checkboxState = it.isSelected
                )
            )
        }

        val param = mapOf(PARAM to cartlistCheckboxStateRequestList)
        val request = GraphqlRequest(SetCartlistCheckboxStateQuery(), SetCartlistCheckboxGqlResponse::class.java, param)
        val response = graphqlRepository.response(listOf(request)).getSuccessData<SetCartlistCheckboxGqlResponse>()
        return response.setCartlistCheckboxStateResponse.data.success == 1
    }
}
