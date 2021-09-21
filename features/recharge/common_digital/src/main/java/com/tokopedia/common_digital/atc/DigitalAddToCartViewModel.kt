package com.tokopedia.common_digital.atc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.common_digital.atc.utils.DigitalAtcMapper
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
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
                                                    private val dispatcher: CoroutineDispatcher,
                                                    private val rechargeAnalytics: RechargeAnalytics)
    : BaseViewModel(dispatcher) {

    private val _addToCartResult = MutableLiveData<Result<String>>()
    val addToCartResult: LiveData<Result<String>>
        get() = _addToCartResult

    fun addToCart(digitalCheckoutPassData: DigitalCheckoutPassData,
                  digitalIdentifierParam: RequestBodyIdentifier,
                  digitalSubscriptionParams: DigitalSubscriptionParams) {
        if (!userSession.isLoggedIn) {
            _addToCartResult.postValue(Fail(MessageErrorException(MESSAGE_ERROR_NON_LOGIN)))
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
                    rechargeAnalytics.eventAddToCart(DigitalAtcMapper.mapToDigitalAtcTrackingModel(restResponse,
                            digitalCheckoutPassData, userSession.userId))
                    _addToCartResult.postValue(Success(restResponse.relationships?.category?.data?.id
                            ?: ""))
                } else _addToCartResult.postValue(Fail(Throwable(DigitalFailGetCartId())))

            }) {
                if (it is ResponseErrorException && !it.message.isNullOrEmpty()) {
                    _addToCartResult.postValue(Fail(MessageErrorException(it.message)))
                } else {
                    _addToCartResult.postValue(Fail(it))
                }
            }
        }
    }

    class DigitalFailGetCartId : Exception()

    companion object {
        const val MESSAGE_ERROR_NON_LOGIN = "Silakan login terlebih dahulu"
    }
}