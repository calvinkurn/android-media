package com.tokopedia.digital_checkout.presentation.viewmodel

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_checkout.data.response.atc.DigitalSubscriptionParams
import com.tokopedia.digital_checkout.data.response.atc.ResponseCartData
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.digital_checkout.usecase.DigitalAddToCartUseCase
import com.tokopedia.digital_checkout.usecase.DigitalGetCartUseCase
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * @author by jessica on 08/01/21
 */

class DigitalCartViewModel @Inject constructor(
        private val digitalGetCartUseCase: DigitalGetCartUseCase,
        private val digitalAddToCartUseCase: DigitalAddToCartUseCase,
        private val userSession: UserSessionInterface,
        dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    fun getCart(digitalCheckoutPassData: DigitalCheckoutPassData) {
        if (!userSession.isLoggedIn) {

        } else {
            //show loading
            //start performance monitoring trace
            digitalCheckoutPassData.categoryId?.let { categoryId ->
                digitalGetCartUseCase.execute(
                        DigitalGetCartUseCase.createParams(categoryId.toInt()),
                        onSuccessGetCart(),
                        onErrorGetCart()
                )
            }
        }
    }

    fun addToCart(digitalCheckoutPassData: DigitalCheckoutPassData,
                  digitalIdentifierParam: RequestBodyIdentifier,
                  digitalSubscriptionParams: DigitalSubscriptionParams) {
        if (!userSession.isLoggedIn) {

        } else {
            val requestParams: RequestParams = digitalAddToCartUseCase.createRequestParams(
                    DigitalAddToCartUseCase.getRequestBodyAtcDigital(
                            digitalCheckoutPassData,
                            userSession.userId.toInt(),
                            digitalIdentifierParam,
                            digitalSubscriptionParams
                    ), digitalCheckoutPassData.idemPotencyKey)
            digitalAddToCartUseCase.execute(requestParams, getSubscriberCart())
        }

    }

    private fun onSuccessGetCart(): (RechargeGetCart.Response) -> Unit {
        return { }
    }

    private fun onErrorGetCart(): (Throwable) -> Unit {
        return { }
    }

    private fun getSubscriberCart(): Subscriber<Map<Type, RestResponse>> {
        return object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
                //if error update livedata
            }

            override fun onNext(typeRestResponseMap: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type
                val restResponse = typeRestResponseMap[token]
                val data = restResponse!!.getData<DataResponse<*>>()
                val responseCartData: ResponseCartData = data.data as ResponseCartData
                //if success update live data
            }
        }
    }
}