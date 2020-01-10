package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.features.cart.data.model.request.UpdateCartRequest
import com.tokopedia.purchase_platform.features.cart.data.model.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-12-26.
 */

class UpdateCartUseCase @Inject constructor(private val schedulers: ExecutorSchedulers) : UseCase<UpdateCartData>() {

    companion object {
        val PARAM_UPDATE_CART_REQUEST = "PARAM_UPDATE_CART_REQUEST"
        val PARAM_CARTS = "carts"

        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_VALUE_ID = "id"

        private val QUERY = """
        mutation update_cart_v2(${'$'}carts: [ParamsCartUpdateCartV2Type], ${'$'}lang: String) {
            update_cart_v2(carts: ${'$'}carts, lang: ${'$'}lang) {
                error_message
                status
                data {
                    error
                    status 
                }
            }
        }
        """.trimIndent()
    }

    override fun createObservable(requestParams: RequestParams?): Observable<UpdateCartData> {
        val paramUpdateList = requestParams?.getObject(PARAM_UPDATE_CART_REQUEST) as ArrayList<UpdateCartRequest>

        val variables = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_CARTS to paramUpdateList
        )

        val graphqlRequest = GraphqlRequest(QUERY, UpdateCartGqlResponse::class.java, variables)
        val graphqlUseCase = GraphqlUseCase()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val updateCartGqlResponse = it.getData<UpdateCartGqlResponse>(UpdateCartGqlResponse::class.java)
                    val updateCartData = UpdateCartData()
                    updateCartData.isSuccess = updateCartGqlResponse.updateCartDataResponse.status == "OK"
                    updateCartData.message = if (updateCartGqlResponse.updateCartDataResponse.status == "OK") {
                        if (updateCartGqlResponse.updateCartDataResponse.data?.error?.isNotBlank() == true) {
                            updateCartGqlResponse.updateCartDataResponse.data.error
                        } else {
                            ""
                        }
                    } else {
                        ""
                    }
                    updateCartData
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

}