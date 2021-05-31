package com.tokopedia.digital_checkout.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.STRING_KODE_PROMO
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.STRING_SUBTOTAL_TAGIHAN
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.SUMMARY_PROMO_CODE_POSITION
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.SUMMARY_TOTAL_PAYMENT_POSITION
import com.tokopedia.digital_checkout.data.PaymentSummary
import com.tokopedia.digital_checkout.data.PaymentSummary.Payment
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
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
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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

    fun getCart(categoryId: String,
                errorNotLoginMessage: String = "") {
        if (!userSession.isLoggedIn) {
            _errorMessage.postValue(errorNotLoginMessage)
        } else {
            _showContentCheckout.postValue(false)
            _showLoading.postValue(true)
            digitalGetCartUseCase.execute(
                    DigitalGetCartUseCase.createParams(categoryId.toIntOrZero()),
                    onSuccessGetCart(categoryId),
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

    private fun onSuccessGetCart(categoryId: String): (RechargeGetCart.Response) -> Unit {
        return {
            val mappedCartData = DigitalCheckoutMapper.mapGetCartToCartDigitalInfoData(it)
            mapDataSuccessCart(mappedCartData, categoryId)
        }
    }

    private fun onErrorGetCart(): (Throwable) -> Unit {
        return {
            handleError(it)
        }
    }

    private fun mapDataSuccessCart(mappedCartData: CartDigitalInfoData, categoryId: String) {
        analytics.eventCheckout(mappedCartData, userSession.userId, categoryId)

        requestCheckoutParam = DigitalCheckoutMapper.buildCheckoutData(mappedCartData, userSession.accessToken, requestCheckoutParam)

        if (mappedCartData.isNeedOtp) {
            _isNeedOtp.postValue(userSession.phoneNumber)
        } else {

            val pricePlain = mappedCartData.attributes.pricePlain
            _totalPrice.postValue(pricePlain)
            paymentSummary.summaries.clear()
            paymentSummary.addToSummary(SUMMARY_TOTAL_PAYMENT_POSITION, Payment(STRING_SUBTOTAL_TAGIHAN, getStringIdrFormat(pricePlain)))
            _payment.postValue(paymentSummary)

            requestCheckoutParam.transactionAmount = pricePlain

            _cartDigitalInfoData.postValue(mappedCartData)

            val promoData = DigitalCheckoutMapper.mapToPromoData(mappedCartData)
            promoData?.let {
                _promoData.postValue(it)
            }
            _showContentCheckout.postValue(true)
            _showLoading.postValue(false)
        }
    }

    fun handleError(e: Throwable) {
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
        _showLoading.postValue(false)
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
        resetCheckoutSummaryPromoAndTotalPrice()
        val promoDataValue = promoData.value?.amount ?: 0
        if (promoDataValue > 0) {
            paymentSummary.addToSummary(SUMMARY_PROMO_CODE_POSITION, Payment(STRING_KODE_PROMO, String.format("-%s", getStringIdrFormat(promoDataValue.toDouble()))))
            _payment.postValue(paymentSummary)
            _totalPrice.forceRefresh()
        } else {
            paymentSummary.removeFromSummary(STRING_KODE_PROMO)
            _payment.postValue(paymentSummary)
        }
    }

    fun resetCheckoutSummaryPromoAndTotalPrice() {
        paymentSummary.removeFromSummary(STRING_KODE_PROMO)
        _payment.postValue(paymentSummary)
        _totalPrice.forceRefresh()
    }

    fun onSubscriptionChecked(isChecked: Boolean) {
        requestCheckoutParam.isSubscriptionChecked = isChecked
    }

    fun onFintechProductChecked(fintechProduct: FintechProduct, isChecked: Boolean, inputPrice: Double?) {
        if (requestCheckoutParam.fintechProducts.containsKey(fintechProduct.tierId) && !isChecked) {
            //remove
            requestCheckoutParam.fintechProducts.remove(fintechProduct.tierId)
        } else if (!requestCheckoutParam.fintechProducts.containsKey(fintechProduct.tierId) && isChecked) {
            //add
            requestCheckoutParam.fintechProducts[fintechProduct.tierId] = fintechProduct
        }
        updateTotalPriceWithFintechProduct(inputPrice)
        updateCheckoutSummaryWithFintechProduct(fintechProduct, isChecked)
    }

    fun updateTotalPriceWithFintechProduct(inputPrice: Double?) {
        cartDigitalInfoData.value?.attributes?.let { attributes ->
            var totalPrice = inputPrice ?: attributes.pricePlain

            requestCheckoutParam.fintechProducts.forEach { fintech ->
                totalPrice += fintech.value.fintechAmount
            }
            _totalPrice.postValue(totalPrice)
        }
    }

    fun updateCheckoutSummaryWithFintechProduct(fintechProduct: FintechProduct, isChecked: Boolean) {
        if (isChecked) {
            paymentSummary.addToSummary(Payment(fintechProduct.transactionType, getStringIdrFormat(fintechProduct.fintechAmount)))
        } else {
            paymentSummary.removeFromSummary(fintechProduct.transactionType)
        }
        _payment.postValue(paymentSummary)
    }

    fun setTotalPriceBasedOnUserInput(totalPrice: Double) {
        requestCheckoutParam.transactionAmount = totalPrice
        updateTotalPriceWithFintechProduct(totalPrice)
    }

    fun setSubtotalPaymentSummaryOnUserInput(totalPrice: Double) {
        paymentSummary.changeSummaryValue(STRING_SUBTOTAL_TAGIHAN, getStringIdrFormat(totalPrice))
        _payment.postValue(paymentSummary)
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

                    _paymentPassData.postValue(checkoutData)

                    requestCheckoutParam.fintechProducts.let { fintech ->
                        if (fintech.isNotEmpty()) {
                            fintech.values.forEach {
                                if (it.info.iconUrl.isNotEmpty()) {
                                    analytics.eventProceedCheckoutTebusMurah(it, cartDigitalInfoData.attributes.categoryName, userSession.userId)
                                } else {
                                    analytics.eventProceedCheckoutCrossell(it, cartDigitalInfoData.attributes.categoryName, userSession.userId)
                                }
                            }
                        }
                    }

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
        if (promoData.state == TickerCheckoutView.State.FAILED || promoData.state == TickerCheckoutView.State.EMPTY) {
            resetCheckoutSummaryPromoAndTotalPrice()
        } else if (promoData.state == TickerCheckoutView.State.ACTIVE) {
            onReceivedPromoCode()
        }
    }

    private fun <T> MutableLiveData<T>.forceRefresh() {
        this.value = this.value
    }
}