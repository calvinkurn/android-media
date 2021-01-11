package com.tokopedia.digital_checkout.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_checkout.data.model.CartItemDigital
import com.tokopedia.digital_checkout.data.model.CartItemDigitalWithTitle
import com.tokopedia.digital_checkout.data.response.atc.DigitalSubscriptionParams
import com.tokopedia.digital_checkout.data.response.atc.ResponseCartData
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.digital_checkout.usecase.DigitalAddToCartUseCase
import com.tokopedia.digital_checkout.usecase.DigitalGetCartUseCase
import com.tokopedia.digital_checkout.utils.DigitalCheckoutMapper
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.ResponseDataNullException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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

    private val _cartTitle = MutableLiveData<String>()
    val cartTitle: LiveData<String>
        get() = _cartTitle

    private val _cartItemDigitalList = MutableLiveData<List<CartItemDigital>>()
    val cartItemDigitalList: LiveData<List<CartItemDigital>>
        get() = _cartItemDigitalList

    private val _cartAdditionalInfoList = MutableLiveData<List<CartItemDigitalWithTitle>>()
    val cartAdditionalInfoList: LiveData<List<CartItemDigitalWithTitle>>
        get() = _cartAdditionalInfoList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _isNeedOtp = MutableLiveData<Boolean>()
    val isNeedOtp: LiveData<Boolean>
        get() = _isNeedOtp

    fun getCart(digitalCheckoutPassData: DigitalCheckoutPassData,
                errorNotLoginMessage: String = "") {
        if (!userSession.isLoggedIn) {
            _errorMessage.postValue(errorNotLoginMessage)
        } else {
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
                  digitalSubscriptionParams: DigitalSubscriptionParams,
                  errorNotLoginMessage: String = "") {
        if (!userSession.isLoggedIn) {
            _errorMessage.postValue(errorNotLoginMessage)
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
                if (e is UnknownHostException) {
                    _errorMessage.postValue(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL)
                } else if (e is SocketTimeoutException || e is ConnectException) {
                    _errorMessage.postValue(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT)
                } else if (e is ResponseErrorException) {
                    _errorMessage.postValue(e.message)
                } else if (e is ResponseDataNullException) {
                    _errorMessage.postValue(e.message)
                } else if (e is HttpErrorException) {
                    _errorMessage.postValue(e.message)
                } else {
                    _errorMessage.postValue(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
                }
            }

            override fun onNext(typeRestResponseMap: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type
                val restResponse = typeRestResponseMap[token]
                val data = restResponse!!.getData<DataResponse<*>>()
                val responseCartData: ResponseCartData = data.data as ResponseCartData

                if (responseCartData.attributes?.isNeedOtp == true) {
                    _isNeedOtp.postValue(true)
                } else {
                    _cartTitle.postValue(responseCartData.attributes?.categoryName ?: "")
                    _cartItemDigitalList.postValue(DigitalCheckoutMapper.mapDigitalInfo(
                            responseCartData.attributes?.mainInfo ?: listOf()))
                    _cartAdditionalInfoList.postValue(DigitalCheckoutMapper.mapAdditionalInfo(
                            responseCartData.attributes?.additionalInfo ?: listOf()))
                }
            }
        }
    }
}