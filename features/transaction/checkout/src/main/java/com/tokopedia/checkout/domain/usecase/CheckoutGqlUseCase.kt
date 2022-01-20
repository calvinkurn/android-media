package com.tokopedia.checkout.domain.usecase

import com.tokopedia.checkout.data.model.response.checkout.CheckoutGqlResponse
import com.tokopedia.checkout.domain.mapper.CheckoutMapper
import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_ERROR_GLOBAL
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class CheckoutGqlUseCase @Inject constructor(@Named(CHECKOUT_MUTATION) private val queryString: String,
                                             private val graphqlUseCase: GraphqlUseCase,
                                             private val checkoutMapper: CheckoutMapper,
                                             private val schedulers: ExecutorSchedulers) : UseCase<CheckoutData>() {

    companion object {
        const val CHECKOUT_MUTATION = "CHECKOUT_MUTATION"
        const val CHECKOUT_PARAM_CARTS = "carts"

        const val PARAM_FINGERPRINT_SUPPORT = "fingerprint_support"
        const val PARAM_FINGERPRINT_PUBLICKEY = "fingerprint_publickey"
        const val PARAM_CARTS = "carts"
        const val PARAM_OPTIONAL = "optional"
        const val PARAM_IS_ONE_CLICK_SHIPMENT = "is_one_click_shipment"
        const val PARAM_IS_THANKYOU_NATIVE_NEW = "is_thankyou_native_new"
        const val PARAM_IS_THANKYOU_NATIVE = "is_thankyou_native"
        const val PARAM_IS_EXPRESS = "is_express"
        const val PARAM_IS_TRADE_IN = "is_trade_in"
        const val PARAM_IS_TRADE_IN_DROP_OFF = "is_trade_in_drop_off"
        const val PARAM_DEV_ID = "dev_id"
    }

    override fun createObservable(requestParam: RequestParams): Observable<CheckoutData> {
        val params = mapOf(CHECKOUT_PARAM_CARTS to requestParam.parameters)
        val graphqlRequest = GraphqlRequest(queryString, CheckoutGqlResponse::class.java, params)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val gqlResponse = it.getData<CheckoutGqlResponse>(CheckoutGqlResponse::class.java)
                    if (gqlResponse != null) {
                        checkoutMapper.convertCheckoutData(gqlResponse.checkout)
                    } else {
                        throw CartResponseErrorException(CART_ERROR_GLOBAL)
                    }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

}