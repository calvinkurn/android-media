package com.tokopedia.digital_checkout.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigital
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigitalWithTitle
import com.tokopedia.digital_checkout.data.request.RequestBodyOtpSuccess
import com.tokopedia.digital_checkout.data.response.CancelVoucherData
import com.tokopedia.digital_checkout.data.response.ResponseCheckout
import com.tokopedia.digital_checkout.data.response.ResponsePatchOtpSuccess
import com.tokopedia.digital_checkout.data.response.atc.DigitalSubscriptionParams
import com.tokopedia.digital_checkout.data.response.atc.ResponseCartData
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.digital_checkout.usecase.*
import com.tokopedia.digital_checkout.utils.DeviceUtil
import com.tokopedia.digital_checkout.utils.DigitalCheckoutMapper
import com.tokopedia.digital_checkout.utils.DigitalCheckoutMapper.getRequestBodyCheckout
import com.tokopedia.digital_checkout.utils.DigitalCurrencyUtil.getStringIdrFormat
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.ResponseDataNullException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

/**
 * @author by jessica on 08/01/21
 */

class DigitalCartViewModel @Inject constructor(
        private val digitalGetCartUseCase: DigitalGetCartUseCase,
        private val digitalAddToCartUseCase: DigitalAddToCartUseCase,
        private val cancelVoucherUseCase: DigitalCancelVoucherUseCase,
        private val digitalPatchOtpUseCase: DigitalPatchOtpUseCase,
        private val digitalCheckoutUseCase: DigitalCheckoutUseCase,
        private val userSession: UserSessionInterface,
        dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _cartDigitalInfoData = MutableLiveData<CartDigitalInfoData>()
    val cartDigitalInfoData: LiveData<CartDigitalInfoData>
        get() = _cartDigitalInfoData

    private val _cartAdditionalInfoList = MutableLiveData<List<CartItemDigitalWithTitle>>()
    val cartAdditionalInfoList: LiveData<List<CartItemDigitalWithTitle>>
        get() = _cartAdditionalInfoList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _isNeedOtp = MutableLiveData<String>()
    val isNeedOtp: LiveData<String>
        get() = _isNeedOtp

    private val _isSuccessCancelVoucherCart = MutableLiveData<Result<Boolean>>()
    val isSuccessCancelVoucherCart: LiveData<Result<Boolean>>
        get() = _isSuccessCancelVoucherCart

    private val _totalPrice = MutableLiveData<Long>()
    val totalPrice: LiveData<Long>
        get() = _totalPrice

    private val _showContentCheckout = MutableLiveData<Boolean>()
    val showContentCheckout: LiveData<Boolean>
        get() = _showContentCheckout

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val _checkoutData = MutableLiveData<PaymentPassData>()
    val checkoutData: LiveData<PaymentPassData>
        get() = _checkoutData

    fun getCart(digitalCheckoutPassData: DigitalCheckoutPassData,
                errorNotLoginMessage: String = "") {
        if (!userSession.isLoggedIn) {
            _errorMessage.postValue(errorNotLoginMessage)
        } else {
            _showContentCheckout.postValue(false)
            _showLoading.postValue(true)
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
            _showContentCheckout.postValue(false)
            _showLoading.postValue(true)
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

    fun processPatchOtpCart(digitalIdentifierParam: RequestBodyIdentifier,
                            digitalCheckoutPassData: DigitalCheckoutPassData,
                            errorNotLoginMessage: String = "") {
        val cartId = _cartDigitalInfoData.value?.id ?: ""
        val attributes = RequestBodyOtpSuccess.Attributes(DeviceUtil.localIpAddress, DeviceUtil.userAgentForApiCall, digitalIdentifierParam)
        val requestBodyOtpSuccess = RequestBodyOtpSuccess("cart", cartId, attributes)
        val requestParams = digitalPatchOtpUseCase.createRequestParams(requestBodyOtpSuccess)
        digitalPatchOtpUseCase.execute(requestParams, getSubscriberOtp(digitalCheckoutPassData, errorNotLoginMessage))
    }

    private fun onSuccessGetCart(): (RechargeGetCart.Response) -> Unit {
        return {
            _showContentCheckout.postValue(true)
            _showLoading.postValue(false)

            val mappedCartData = DigitalCheckoutMapper.mapGetCartToCartDigitalInfoData(it)

            if (mappedCartData.isNeedOtp) {
                _isNeedOtp.postValue(userSession.phoneNumber)
            } else {
                _showContentCheckout.postValue(true)
                _showLoading.postValue(false)
                _cartDigitalInfoData.postValue(mappedCartData)
                _cartAdditionalInfoList.postValue(mappedCartData.additionalInfos)
                _totalPrice.postValue(mappedCartData.attributes?.pricePlain ?: 0)
            }
        }
    }

    private fun onErrorGetCart(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _showLoading.postValue(false)
            errorHandler(it)
        }
    }

    private fun getSubscriberCart(): Subscriber<Map<Type, RestResponse>> {
        return object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
                _showLoading.postValue(false)
                errorHandler(e)
            }

            override fun onNext(typeRestResponseMap: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type
                val restResponse = typeRestResponseMap[token]
                val data = restResponse!!.getData<DataResponse<*>>()
                val responseCartData: ResponseCartData = data.data as ResponseCartData
                val mappedCartData = DigitalCheckoutMapper.mapToCartDigitalInfoData(responseCartData)

                if (mappedCartData.isNeedOtp) {
                    _isNeedOtp.postValue(userSession.phoneNumber)
                } else {
                    _showContentCheckout.postValue(true)
                    _showLoading.postValue(false)
                    _cartDigitalInfoData.postValue(mappedCartData)
                    _cartAdditionalInfoList.postValue(mappedCartData.additionalInfos)
                    _totalPrice.postValue(mappedCartData.attributes?.pricePlain ?: 0)
                }
            }
        }
    }


    private fun getSubscriberOtp(digitalCheckoutPassData: DigitalCheckoutPassData,
                                 errorNotLoginMessage: String = ""): Subscriber<Map<Type, RestResponse>> {
        return object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
            }

            override fun onNext(typeRestResponseMap: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<ResponsePatchOtpSuccess>>() {}.type
                val restResponse = typeRestResponseMap[token]
                val data = restResponse!!.getData<DataResponse<*>>()
                val responsePatchOtpSuccess = data.data as ResponsePatchOtpSuccess

                if (responsePatchOtpSuccess.success) {
                    return getCart(digitalCheckoutPassData, errorNotLoginMessage)
                }
            }
        }
    }

    fun errorHandler(e: Throwable) {
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

    fun cancelVoucherCart() {
        cancelVoucherUseCase.execute(onSuccessCancelVoucher(), onErrorCancelVoucher())
    }

    private fun onSuccessCancelVoucher(): (CancelVoucherData.Response) -> Unit {
        return {
            if (it.response.success) {
                _isSuccessCancelVoucherCart.postValue(Success(true))
            } else {
                _isSuccessCancelVoucherCart.postValue(Fail(Throwable("")))
            }
        }
    }

    private fun onErrorCancelVoucher(): (Throwable) -> Unit {
        return {
            _isSuccessCancelVoucherCart.postValue(Fail(it))
        }
    }

    fun onReceivedPromoCode(promoData: PromoData) {
        resetVoucherCart()
        if (promoData.amount > 0) {
            val additionals: MutableList<CartItemDigitalWithTitle> = ArrayList(_cartAdditionalInfoList.value
                    ?: listOf())
            val items: MutableList<CartItemDigital> = ArrayList()
            items.add(CartItemDigital("Harga", cartDigitalInfoData.value?.attributes?.price ?: ""))
            items.add(CartItemDigital("Promo", String.format("-%s", getStringIdrFormat(promoData.amount.toDouble()))))
            val totalPayment: Long = (cartDigitalInfoData.value?.attributes?.pricePlain
                    ?: 0L) - promoData.amount.toLong()
            items.add(CartItemDigital("Total Bayar", getStringIdrFormat(totalPayment.toDouble())))
            val cartAdditionalInfo = CartItemDigitalWithTitle("Pembayaran", items)
            additionals.add(cartAdditionalInfo)
            _cartAdditionalInfoList.value = additionals
            _totalPrice.value = cartDigitalInfoData.value?.attributes?.pricePlain ?: 0L
        }
    }

    fun resetVoucherCart() {
        val additionalInfos = cartAdditionalInfoList.value?.toMutableList() ?: mutableListOf()
        for ((i, additionalInfo) in additionalInfos.withIndex()) {
            if (additionalInfo.title.contains("Pembayaran")) {
                additionalInfos.removeAt(i)
                break
            }
        }
        _cartAdditionalInfoList.value = additionalInfos
        _totalPrice.value = cartDigitalInfoData.value?.attributes?.pricePlain ?: 0L
    }

    fun updateTotalPriceWithFintechProduct(isChecked: Boolean) {
        cartDigitalInfoData.value?.attributes?.let { attributes ->
            var totalPrice = attributes.pricePlain
            if (isChecked) {
                val fintechProductPrice = attributes.fintechProduct?.getOrNull(0)?.fintechAmount
                        ?: 0
                totalPrice += fintechProductPrice
            }
            _totalPrice.value = totalPrice
        }
    }

    fun setTotalPrice(totalPrice: Long) {
        _totalPrice.postValue(totalPrice)
    }

    fun proceedToCheckout(promoCode: String, digitalIdentifierParam: RequestBodyIdentifier) {
        val cartDigitalInfoData = _cartDigitalInfoData.value
        cartDigitalInfoData?.let {
            val checkoutData = DigitalCheckoutMapper.buildCheckoutData(it, userSession.accessToken)
            checkoutData.voucherCode = promoCode
            _showLoading.postValue(true)
            if (checkoutData.isNeedOtp) {
                _isNeedOtp.postValue(userSession.phoneNumber)
            }
            val requestParams: RequestParams = digitalCheckoutUseCase.createRequestParams(getRequestBodyCheckout(checkoutData, digitalIdentifierParam))
            digitalCheckoutUseCase.execute(requestParams, getSubscriberCheckout())
        }
    }

    private fun getSubscriberCheckout(): Subscriber<Map<Type, RestResponse>> {
        return object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
                _showLoading.postValue(false)
            }

            override fun onNext(checkoutDigitalData: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<ResponseCheckout>>() {}.type
                val restResponse = checkoutDigitalData[token]
                val data = restResponse!!.getData<DataResponse<*>>()
                val responseCheckoutData = data.data as ResponseCheckout
                val checkoutData = DigitalCheckoutMapper.mapToPaymentPassData(responseCheckoutData)

                _showLoading.postValue(false)
                _checkoutData.postValue(checkoutData)
            }
        }
    }
}