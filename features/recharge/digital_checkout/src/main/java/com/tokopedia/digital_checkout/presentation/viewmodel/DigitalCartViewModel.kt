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
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.STRING_KODE_PROMO
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.STRING_SUBTOTAL_TAGIHAN
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigital
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigitalWithTitle
import com.tokopedia.digital_checkout.data.request.DigitalCheckoutDataParameter
import com.tokopedia.digital_checkout.data.request.RequestBodyOtpSuccess
import com.tokopedia.digital_checkout.data.response.CancelVoucherData
import com.tokopedia.digital_checkout.data.response.ResponseCheckout
import com.tokopedia.digital_checkout.data.response.ResponsePatchOtpSuccess
import com.tokopedia.digital_checkout.data.response.atc.DigitalSubscriptionParams
import com.tokopedia.digital_checkout.data.response.atc.ResponseCartData
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.digital_checkout.presentation.adapter.DigitalCheckoutSummaryAdapter
import com.tokopedia.digital_checkout.presentation.adapter.DigitalCheckoutSummaryAdapter.Payment
import com.tokopedia.digital_checkout.presentation.adapter.DigitalCheckoutSummaryAdapter.PaymentSummary
import com.tokopedia.digital_checkout.usecase.*
import com.tokopedia.digital_checkout.utils.DeviceUtil
import com.tokopedia.digital_checkout.utils.DigitalCheckoutMapper
import com.tokopedia.digital_checkout.utils.DigitalCheckoutMapper.getRequestBodyCheckout
import com.tokopedia.digital_checkout.utils.DigitalCurrencyUtil.getStringIdrFormat
import com.tokopedia.digital_checkout.utils.analytics.DigitalAnalytics
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.ResponseDataNullException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
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
        private val analytics: DigitalAnalytics,
        private val digitalGetCartUseCase: DigitalGetCartUseCase,
        private val digitalAddToCartUseCase: DigitalAddToCartUseCase,
        private val cancelVoucherUseCase: DigitalCancelVoucherUseCase,
        private val digitalPatchOtpUseCase: DigitalPatchOtpUseCase,
        private val digitalCheckoutUseCase: DigitalCheckoutUseCase,
        private val userSession: UserSessionInterface,
        private val dispatcher: CoroutineDispatcher,
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

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double>
        get() = _totalPrice

    private val _showContentCheckout = MutableLiveData<Boolean>()
    val showContentCheckout: LiveData<Boolean>
        get() = _showContentCheckout

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val _paymentPassData = MutableLiveData<PaymentPassData>()
    val paymentPassData: LiveData<PaymentPassData>
        get() = _paymentPassData

    private val _promoData = MutableLiveData<PromoData>()
    val promoData: LiveData<PromoData>
        get() = _promoData

    private val _payment = MutableLiveData<PaymentSummary>()
    val payment: LiveData<PaymentSummary>
        get() = _payment

    var requestCheckoutParam = DigitalCheckoutDataParameter()

    private val paymentSummary = PaymentSummary(mutableListOf())

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
                        onSuccessGetCart(digitalCheckoutPassData.source),
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
                onSuccessAddToCart(data, digitalCheckoutPassData.source)

            }) {
                _showLoading.postValue(false)
                errorHandler(it)
            }
        }
    }

    private fun onSuccessAddToCart(data: Map<Type, RestResponse?>, source: Int) {

        val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type
        val restResponse = data[token]
        val lala = restResponse!!.getData<DataResponse<*>>()
        val responseCartData: ResponseCartData = lala.data as ResponseCartData
        val mappedCartData = DigitalCheckoutMapper.mapToCartDigitalInfoData(responseCartData)

        mapDataSuccessCart(source, mappedCartData)
    }

    fun processPatchOtpCart(digitalIdentifierParam: RequestBodyIdentifier,
                            digitalCheckoutPassData: DigitalCheckoutPassData,
                            errorNotLoginMessage: String = "") {
        launchCatchError(block = {
            val otpResponse = withContext(dispatcher) {
                val attributes = RequestBodyOtpSuccess.Attributes(DeviceUtil.localIpAddress, DeviceUtil.userAgentForApiCall, digitalIdentifierParam)
                val requestBodyOtpSuccess = RequestBodyOtpSuccess(DigitalCheckoutConst.RequestBodyParams.REQUEST_BODY_OTP_CART_TYPE,
                        requestCheckoutParam.cartId, attributes)
                digitalPatchOtpUseCase.setRequestParams(requestBodyOtpSuccess)
                digitalPatchOtpUseCase.executeOnBackground()
            }

            val token = object : TypeToken<DataResponse<ResponsePatchOtpSuccess>>() {}.type
            val restResponse = otpResponse[token]
            val data = restResponse!!.getData<DataResponse<*>>()
            val responsePatchOtpSuccess = data.data as ResponsePatchOtpSuccess

            if (responsePatchOtpSuccess.success) {
                getCart(digitalCheckoutPassData, errorNotLoginMessage)
            }

        }) {
            _showLoading.postValue(false)
            errorHandler(it)
        }
    }

    private fun onSuccessGetCart(source: Int): (RechargeGetCart.Response) -> Unit {
        return {
            _showContentCheckout.postValue(true)
            _showLoading.postValue(false)

            val mappedCartData = DigitalCheckoutMapper.mapGetCartToCartDigitalInfoData(it)
            mapDataSuccessCart(source, mappedCartData)
        }
    }

    private fun onErrorGetCart(): (Throwable) -> Unit {
        return {
            _showLoading.postValue(false)
            errorHandler(it)
        }
    }

    private fun mapDataSuccessCart(source: Int, mappedCartData: CartDigitalInfoData) {
        analytics.eventAddToCart(mappedCartData, source, userSession.userId)
        analytics.eventCheckout(mappedCartData, userSession.userId)

        requestCheckoutParam = DigitalCheckoutMapper.buildCheckoutData(mappedCartData, userSession.accessToken, requestCheckoutParam)

        if (mappedCartData.isNeedOtp) {
            _isNeedOtp.postValue(userSession.phoneNumber)
        } else {
            _showContentCheckout.postValue(true)
            _showLoading.postValue(false)
            _cartDigitalInfoData.postValue(mappedCartData)
            _cartAdditionalInfoList.postValue(mappedCartData.additionalInfos)

            val pricePlain = mappedCartData.attributes.pricePlain
            _totalPrice.postValue(pricePlain)
            paymentSummary.addToSummary(Payment(STRING_SUBTOTAL_TAGIHAN, getStringIdrFormat(pricePlain)))
            _payment.postValue(paymentSummary)

            requestCheckoutParam.transactionAmount = pricePlain

            val promoData = DigitalCheckoutMapper.mapToPromoData(mappedCartData)
            promoData?.let {
                _promoData.postValue(it)
            }
        }
    }

    private fun errorHandler(e: Throwable) {
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
                setPromoData(PromoData(state = TickerCheckoutView.State.EMPTY, description = ""))
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

    private fun onReceivedPromoCode() {
        resetAdditionalInfoAndTotalPrice()
        val promoDataValue = promoData.value?.amount ?: 0
        if (promoDataValue > 0) {
            paymentSummary.addToSummary(Payment(STRING_KODE_PROMO, String.format("-%s", getStringIdrFormat(promoDataValue.toDouble()))))
            _payment.postValue(paymentSummary)
            _totalPrice.forceRefresh()
        } else {
            paymentSummary.removeFromSummary(STRING_KODE_PROMO)
            _payment.postValue(paymentSummary)
        }
    }

    // TODO: change function name
    fun resetAdditionalInfoAndTotalPrice() {
        paymentSummary.removeFromSummary(STRING_KODE_PROMO)
        _payment.postValue(paymentSummary)
        _totalPrice.forceRefresh()
    }

    fun onSubscriptionChecked(isChecked: Boolean) {
        requestCheckoutParam.isSubscriptionChecked = isChecked
    }

    fun updateTotalPriceWithFintechProduct(isChecked: Boolean, inputPrice: Double?) {
        requestCheckoutParam.isFintechProductChecked = isChecked

        cartDigitalInfoData.value?.attributes?.let { attributes ->
            var totalPrice = inputPrice ?: attributes.pricePlain
            val fintechProductName = attributes.fintechProduct.getOrNull(0)?.info?.title
                    ?: ""
            val fintechProductPrice = attributes.fintechProduct.getOrNull(0)?.fintechAmount
                    ?: 0.0
            if (isChecked) {
                totalPrice += fintechProductPrice
                paymentSummary.addToSummary(Payment(fintechProductName, getStringIdrFormat(fintechProductPrice)))
            } else {
                paymentSummary.removeFromSummary(fintechProductName)
            }
            _payment.postValue(paymentSummary)
            _totalPrice.postValue(totalPrice)
        }
    }

    fun setTotalPriceBasedOnUserInput(totalPrice: Double, isFintechProductChecked: Boolean) {
        requestCheckoutParam.transactionAmount = totalPrice
        updateTotalPriceWithFintechProduct(isFintechProductChecked, totalPrice)
    }

    fun proceedToCheckout(digitalIdentifierParam: RequestBodyIdentifier) {
        val promoCode = promoData.value?.promoCode ?: ""
        val cartDigitalInfoData = _cartDigitalInfoData.value
        cartDigitalInfoData?.let {
            requestCheckoutParam.voucherCode = promoCode

            _showLoading.postValue(true)

            if (requestCheckoutParam.isNeedOtp) {
                _isNeedOtp.postValue(userSession.phoneNumber)
            } else {
                launchCatchError(block = {
                    val checkoutDigitalData = withContext(dispatcher) {
                        digitalCheckoutUseCase.setRequestParams(
                                getRequestBodyCheckout(requestCheckoutParam, digitalIdentifierParam,
                                        it.attributes.fintechProduct.getOrNull(0)))

                        digitalCheckoutUseCase.executeOnBackground()
                    }

                    val token = object : TypeToken<DataResponse<ResponseCheckout>>() {}.type
                    val restResponse = checkoutDigitalData[token]
                    val data = restResponse!!.getData<DataResponse<*>>()
                    val responseCheckoutData = data.data as ResponseCheckout
                    val checkoutData = DigitalCheckoutMapper.mapToPaymentPassData(responseCheckoutData)

                    _showLoading.postValue(false)
                    _paymentPassData.postValue(checkoutData)

                }) {
                    _showLoading.postValue(false)
                    errorHandler(it)
                }
            }
        }
    }

    fun getPromoDigitalModel(cartPassData: DigitalCheckoutPassData?, userInputPriceAmount: Double?): PromoDigitalModel {
        val cartInfoData = cartDigitalInfoData.value ?: CartDigitalInfoData()
        var price: Double = cartInfoData.attributes.pricePlain

        if (userInputPriceAmount != null) {
            price = userInputPriceAmount
        }

        return PromoDigitalModel(cartPassData?.categoryId?.toIntOrNull() ?: 0,
                cartInfoData.attributes.categoryName,
                cartInfoData.attributes.operatorName,
                cartPassData?.productId?.toIntOrNull() ?: 0,
                cartPassData?.clientNumber ?: "",
                price.toLong()
        )
    }

    fun setPromoData(promoData: PromoData) {
        _promoData.value = promoData
    }

    fun applyPromoData(promoData: PromoData) {
        when (promoData.state) {
            TickerCheckoutView.State.FAILED,
            TickerCheckoutView.State.EMPTY -> {
                resetAdditionalInfoAndTotalPrice()
            }
            TickerCheckoutView.State.ACTIVE -> {
                onReceivedPromoCode()
            }
            else -> {}
        }
    }

    private fun <T> MutableLiveData<T>.forceRefresh() {
        this.value = this.value
    }
}