package com.tokopedia.common_digital.atc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by jessica on 04/03/21
 */

class DigitalAddToCartViewModel @Inject constructor(private val digitalAddToCartUseCase: DigitalAddToCartUseCase,
                                                    private val userSession: UserSessionInterface,
                                                    private val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val _addToCartResult = MutableLiveData<Result<String>>()
    val addToCartResult: LiveData<Result<String>>
        get() = _addToCartResult

    fun addToCart(digitalCheckoutPassData: DigitalCheckoutPassData,
                  digitalIdentifierParam: RequestBodyIdentifier,
                  digitalSubscriptionParams: DigitalSubscriptionParams) {
        if (!userSession.isLoggedIn) {
            _addToCartResult.postValue(Fail(DigitalUserNotLoginException()))
        } else {
            launchCatchError(block = {
                val data = withContext(dispatcher) {
                    digitalAddToCartUseCase.setRequestParams(
                            DigitalAddToCartUseCase.getRequestBodyAtcDigital(
                                    digitalCheckoutPassData,
                                    userSession.userId.toInt(),
                                    digitalIdentifierParam,
                                    digitalSubscriptionParams
                            ), digitalCheckoutPassData.idemPotencyKey)
                    digitalAddToCartUseCase.executeOnBackground()
                }

                val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type
                val restResponse = data[token]?.getData<DataResponse<*>>()?.data as ResponseCartData
                if (restResponse.id != null) {
                    _addToCartResult.postValue(Success(digitalCheckoutPassData.categoryId ?: ""))
                } else _addToCartResult.postValue(Fail(DigitalFailGetCartId()))

            }) {
                _addToCartResult.postValue(Fail(it))
            }
        }
    }

    class DigitalUserNotLoginException : Exception()
    class DigitalFailGetCartId : Exception()
}