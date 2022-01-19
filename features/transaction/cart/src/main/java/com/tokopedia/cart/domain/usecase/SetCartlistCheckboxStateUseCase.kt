package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.request.SetCartlistCheckboxStateRequest
import com.tokopedia.cart.data.model.response.cartlistcheckboxstate.SetCartlistCheckboxGqlResponse
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class SetCartlistCheckboxStateUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                          private val schedulers: ExecutorSchedulers) : UseCase<Boolean>() {

    companion object {
        const val PARAM_SET_CARTLIST_CHECKBOX_STATE_REQUEST = "PARAM_SET_CARTLIST_CHECKBOX_STATE_REQUEST"

        private const val PARAM = "params"
    }

    fun buildRequestParams(cartItemDataList: List<CartItemHolderData>): RequestParams {
        val cartlistCheckboxStateRequestList = ArrayList<SetCartlistCheckboxStateRequest>()
        cartItemDataList.forEach {
            cartlistCheckboxStateRequestList.add(
                    SetCartlistCheckboxStateRequest(
                            cartId = it.cartId,
                            checkboxState = it.isSelected
                    )
            )
        }

        val variables = mapOf(PARAM_SET_CARTLIST_CHECKBOX_STATE_REQUEST to cartlistCheckboxStateRequestList)

        return RequestParams.create().apply {
            putAll(variables)
        }
    }

    override fun createObservable(requestParams: RequestParams?): Observable<Boolean?>? {
        val params = requestParams?.getObject(PARAM_SET_CARTLIST_CHECKBOX_STATE_REQUEST) as List<SetCartlistCheckboxStateRequest>
        val variables = mapOf(PARAM to params)

        val mutation = getSetCartlistCheckboxStateMutation()
        val graphqlRequest = GraphqlRequest(mutation, SetCartlistCheckboxGqlResponse::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    var result: Boolean = false
                    it.getData<SetCartlistCheckboxGqlResponse>(SetCartlistCheckboxGqlResponse::class.java)?.let {
                        result = it.setCartlistCheckboxStateResponse.data.success == 1
                        result
                    }

                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

}