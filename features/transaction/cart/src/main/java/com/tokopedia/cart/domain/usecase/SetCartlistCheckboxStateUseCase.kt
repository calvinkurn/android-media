package com.tokopedia.cart.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cart.data.model.request.SetCartlistCheckboxStateRequest
import com.tokopedia.cart.data.model.response.cartlistcheckboxstate.SetCartlistCheckboxGqlResponse
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SetCartlistCheckboxStateUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository
) : UseCase<Boolean>() {

    companion object {
        const val PARAM_SET_CARTLIST_CHECKBOX_STATE_REQUEST = "PARAM_SET_CARTLIST_CHECKBOX_STATE_REQUEST"
        
        const val QUERY_SET_CARTLIST_CHECKBOX_STATE = "SetCartlistCheckboxStateQuery"
    }

    private var params: Map<String, Any> = emptyMap()

    fun setParams(cartItemDataList: List<CartItemHolderData>): SetCartlistCheckboxStateUseCase {
        val cartlistCheckboxStateRequestList = ArrayList<SetCartlistCheckboxStateRequest>()
        cartItemDataList.forEach {
            cartlistCheckboxStateRequestList.add(
                SetCartlistCheckboxStateRequest(
                    cartId = it.cartId,
                    checkboxState = it.isSelected
                )
            )
        }

        params = mapOf(PARAM_SET_CARTLIST_CHECKBOX_STATE_REQUEST to cartlistCheckboxStateRequestList)
        return this
    }
    
    @GqlQuery(QUERY_SET_CARTLIST_CHECKBOX_STATE, SET_CHECKBOX_STATE_QUERY)
    override suspend fun executeOnBackground(): Boolean {
        val request = GraphqlRequest(SetCartlistCheckboxStateQuery(), SetCartlistCheckboxGqlResponse::class.java, params)
        val response = graphqlRepository.response(listOf(request)).getSuccessData<SetCartlistCheckboxGqlResponse>()
        return response.setCartlistCheckboxStateResponse.data.success == 1
    }
}
