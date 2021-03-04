package com.tokopedia.digital_checkout.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigital
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigitalWithTitle
import com.tokopedia.digital_checkout.data.request.DigitalCheckoutDataParameter
import com.tokopedia.digital_checkout.data.request.RequestBodyOtpSuccess
import com.tokopedia.digital_checkout.data.response.CancelVoucherData
import com.tokopedia.digital_checkout.data.response.ResponseCheckout
import com.tokopedia.digital_checkout.data.response.ResponsePatchOtpSuccess
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.digital_checkout.usecase.DigitalCancelVoucherUseCase
import com.tokopedia.digital_checkout.usecase.DigitalCheckoutUseCase
import com.tokopedia.digital_checkout.usecase.DigitalGetCartUseCase
import com.tokopedia.digital_checkout.usecase.DigitalPatchOtpUseCase
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

    var requestCheckoutParam = DigitalCheckoutDataParameter()

    fun getCart(categoryId: String,
                errorNotLoginMessage: String = "") {
        if (!userSession.isLoggedIn) {
            _errorMessage.postValue(errorNotLoginMessage)
        } else {
            _showContentCheckout.postValue(false)
            _showLoading.postValue(true)
            digitalGetCartUseCase.execute(
                    DigitalGetCartUseCase.createParams(categoryId.toInt()),
                    onSuccessGetCart(),
                    onErrorGetCart()
                )
        }
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
                getCart(digitalCheckoutPassData.categoryId ?: "", errorNotLoginMessage)
            }

        }) {
            handleError(it)
        }
    }

    private fun onSuccessGetCart(): (RechargeGetCart.Response) -> Unit {
        return {
            _showContentCheckout.postValue(true)
            _showLoading.postValue(false)

            val mappedCartData = DigitalCheckoutMapper.mapGetCartToCartDigitalInfoData(it)
            mapDataSuccessCart(mappedCartData)
        }
    }

    private fun onErrorGetCart(): (Throwable) -> Unit {
        return {
            handleError(it)
        }
    }

    private fun mapDataSuccessCart(mappedCartData: CartDigitalInfoData) {
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
            requestCheckoutParam.transactionAmount = pricePlain

            val promoData = DigitalCheckoutMapper.mapToPromoData(mappedCartData)
            promoData?.let {
                _promoData.postValue(it)
            }
        }
    }

    fun handleError(e: Throwable) {
        _showLoading.postValue(false)
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
            val additionals: MutableList<CartItemDigitalWithTitle> = ArrayList(_cartAdditionalInfoList.value
                    ?: listOf())
            val items: MutableList<CartItemDigital> = ArrayList()
            items.add(CartItemDigital(DigitalCheckoutConst.AdditionalInfo.STRING_PRICE, cartDigitalInfoData.value?.attributes?.price
                    ?: ""))
            items.add(CartItemDigital(DigitalCheckoutConst.AdditionalInfo.STRING_PROMO, String.format("-%s", getStringIdrFormat(promoDataValue.toDouble()))))
            val totalPayment = (cartDigitalInfoData.value?.attributes?.pricePlain
                    ?: 0.0) - promoDataValue.toDouble()
            items.add(CartItemDigital(DigitalCheckoutConst.AdditionalInfo.STRING_TOTAL_PAYMENT, getStringIdrFormat(totalPayment)))
            val cartAdditionalInfo = CartItemDigitalWithTitle(DigitalCheckoutConst.AdditionalInfo.STRING_PAYMENT, items)
            additionals.add(cartAdditionalInfo)
            _cartAdditionalInfoList.postValue(additionals)
            _totalPrice.forceRefresh()
        }
    }

    fun resetAdditionalInfoAndTotalPrice() {
        val additionalInfos = cartAdditionalInfoList.value?.toMutableList() ?: mutableListOf()
        for ((i, additionalInfo) in additionalInfos.withIndex()) {
            if (additionalInfo.title.contains(DigitalCheckoutConst.AdditionalInfo.STRING_PAYMENT)) {
                additionalInfos.removeAt(i)
                break
            }
        }
        _cartAdditionalInfoList.postValue(additionalInfos)
        _totalPrice.forceRefresh()
    }

    fun onSubscriptionChecked(isChecked: Boolean) {
        requestCheckoutParam.isSubscriptionChecked = isChecked
    }

    fun updateTotalPriceWithFintechProduct(isChecked: Boolean, inputPrice: Double?) {
        requestCheckoutParam.isFintechProductChecked = isChecked

        cartDigitalInfoData.value?.attributes?.let { attributes ->
            var totalPrice = inputPrice ?: attributes.pricePlain
            if (isChecked) {
                val fintechProductPrice = attributes.fintechProduct.getOrNull(0)?.fintechAmount
                        ?: 0.0
                totalPrice += fintechProductPrice
            }
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
                    handleError(it)
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