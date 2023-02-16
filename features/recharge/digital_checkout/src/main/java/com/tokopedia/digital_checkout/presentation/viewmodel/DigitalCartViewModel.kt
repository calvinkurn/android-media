package com.tokopedia.digital_checkout.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.STRING_ADMIN_FEE
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.STRING_KODE_PROMO
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.STRING_SUBTOTAL_TAGIHAN
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.SUMMARY_ADMIN_FEE_POSITION
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.SUMMARY_PROMO_CODE_POSITION
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst.SummaryInfo.SUMMARY_TOTAL_PAYMENT_POSITION
import com.tokopedia.digital_checkout.data.PaymentSummary
import com.tokopedia.digital_checkout.data.PaymentSummary.Payment
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.request.DigitalCheckoutDataParameter
import com.tokopedia.digital_checkout.data.request.DigitalCrossSellData
import com.tokopedia.digital_checkout.data.request.RequestBodyOtpSuccess
import com.tokopedia.digital_checkout.data.response.CancelVoucherData
import com.tokopedia.digital_checkout.data.response.ResponsePatchOtpSuccess
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.digital_checkout.usecase.DigitalCancelVoucherUseCase
import com.tokopedia.digital_checkout.usecase.DigitalCheckoutUseCase
import com.tokopedia.digital_checkout.usecase.DigitalGetCartUseCase
import com.tokopedia.digital_checkout.usecase.DigitalPatchOtpUseCase
import com.tokopedia.digital_checkout.utils.DeviceUtil
import com.tokopedia.digital_checkout.utils.DigitalCheckoutMapper
import com.tokopedia.digital_checkout.utils.DigitalCurrencyUtil.getStringIdrFormat
import com.tokopedia.digital_checkout.utils.analytics.DigitalAnalytics
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
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
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val gson = Gson()

    private val _cartDigitalInfoData = MutableLiveData<CartDigitalInfoData>()
    val cartDigitalInfoData: LiveData<CartDigitalInfoData>
        get() = _cartDigitalInfoData

    private val _errorThrowable = MutableLiveData<Fail>()
    val errorThrowable: LiveData<Fail>
        get() = _errorThrowable

    private val _isNeedOtp = MutableLiveData<String>()
    val isNeedOtp: LiveData<String>
        get() = _isNeedOtp

    private val _cancelVoucherData = MutableLiveData<Result<CancelVoucherData>>()
    val cancelVoucherData: LiveData<Result<CancelVoucherData>>
        get() = _cancelVoucherData

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

    fun getCart(
        categoryId: String,
        errorNotLoginMessage: String = "",
        isSpecialProduct: Boolean
    ) {
        if (!userSession.isLoggedIn) {
            _errorThrowable.postValue(Fail(MessageErrorException(errorNotLoginMessage)))
        } else {
            _showContentCheckout.postValue(false)
            _showLoading.postValue(true)
            digitalGetCartUseCase.onExecute(
                DigitalGetCartUseCase.createParams(categoryId.toIntSafely()),
                onSuccessGetCart(categoryId, isSpecialProduct),
                onErrorGetCart()
            )
        }
    }

    fun processPatchOtpCart(
        digitalIdentifierParam: RequestBodyIdentifier,
        digitalCheckoutPassData: DigitalCheckoutPassData,
        errorNotLoginMessage: String = "",
        isSpecialProduct: Boolean
    ) {
        launchCatchError(block = {
            val otpResponse = withContext(dispatcher) {
                val attributes = RequestBodyOtpSuccess.Attributes(
                    DeviceUtil.localIpAddress,
                    DeviceUtil.userAgentForApiCall,
                    digitalIdentifierParam
                )
                val requestBodyOtpSuccess = RequestBodyOtpSuccess(
                    DigitalCheckoutConst.RequestBodyParams.REQUEST_BODY_OTP_CART_TYPE,
                    requestCheckoutParam.cartId,
                    attributes
                )
                digitalPatchOtpUseCase.setRequestParams(requestBodyOtpSuccess)
                digitalPatchOtpUseCase.executeOnBackground()
            }

            val token = object : TypeToken<DataResponse<ResponsePatchOtpSuccess>>() {}.type
            val restResponse = otpResponse[token]
            val data = restResponse!!.getData<DataResponse<*>>()
            val responsePatchOtpSuccess = data.data as ResponsePatchOtpSuccess

            if (responsePatchOtpSuccess.success) {
                getCart(
                    digitalCheckoutPassData.categoryId ?: "",
                    errorNotLoginMessage,
                    isSpecialProduct
                )
            }
        }) {
            _showLoading.postValue(false)
            if (it is ResponseErrorException && !it.message.isNullOrEmpty()) {
                _errorThrowable.postValue(Fail(MessageErrorException(it.message)))
            } else {
                _errorThrowable.postValue(Fail(it))
            }
        }
    }

    private fun onSuccessGetCart(categoryId: String, isSpecialProduct: Boolean): (RechargeGetCart.Response) -> Unit {
        return {
            val mappedCartData =
                DigitalCheckoutMapper.mapGetCartToCartDigitalInfoData(it, isSpecialProduct)
            mapDataSuccessCart(mappedCartData, categoryId)
        }
    }

    private fun onErrorGetCart(): (Throwable) -> Unit {
        return {
            _showLoading.postValue(false)
            _errorThrowable.postValue(Fail(it))
        }
    }

    private fun mapDataSuccessCart(mappedCartData: CartDigitalInfoData, categoryId: String) {
        analytics.eventCheckout(mappedCartData, userSession.userId, categoryId)

        requestCheckoutParam = DigitalCheckoutMapper.buildCheckoutData(
            mappedCartData,
            userSession.accessToken,
            requestCheckoutParam
        )

        if (mappedCartData.isNeedOtp) {
            _isNeedOtp.postValue(userSession.phoneNumber)
        } else {
            // set up price and also payment summary based on response from BE
            val pricePlain = mappedCartData.attributes.pricePlain
            _totalPrice.postValue(
                calculateTotalPrice(
                    pricePlain,
                    mappedCartData.attributes.adminFee,
                    mappedCartData.attributes.isOpenAmount,
                    mappedCartData.attributes.isAdminFeeIncluded
                )
            )
            paymentSummary.summaries.clear()
            paymentSummary.addToSummary(
                SUMMARY_TOTAL_PAYMENT_POSITION,
                Payment(STRING_SUBTOTAL_TAGIHAN, getStringIdrFormat(pricePlain))
            )

            if (mappedCartData.attributes.isOpenAmount &&
                mappedCartData.attributes.adminFee > 0 &&
                !mappedCartData.attributes.isAdminFeeIncluded
            ) {
                paymentSummary.addToSummary(
                    SUMMARY_ADMIN_FEE_POSITION,
                    Payment(
                        STRING_ADMIN_FEE,
                        getStringIdrFormat(mappedCartData.attributes.adminFee)
                    )
                )
            }
            _payment.postValue(paymentSummary)

            // render checkout page
            requestCheckoutParam.transactionAmount = pricePlain
            requestCheckoutParam.isInstantCheckout = mappedCartData.isInstantCheckout
            _cartDigitalInfoData.postValue(mappedCartData)

            // render promo
            val promo = DigitalCheckoutMapper.mapToPromoData(mappedCartData)
            promo?.let {
                _promoData.postValue(promo)
            }

            // show checkout page
            _showContentCheckout.postValue(true)
            _showLoading.postValue(false)
        }
    }

    fun cancelVoucherCart(promoCode: String, defaultErrorMsg: String) {
        cancelVoucherUseCase.execute(
            promoCode,
            onSuccessCancelVoucher(defaultErrorMsg),
            onErrorCancelVoucher(defaultErrorMsg)
        )
    }

    private fun calculateTotalPrice(
        totalPrice: Double,
        adminFee: Double,
        isOpenAmount: Boolean,
        isAdminFeeIncluded: Boolean
    ): Double {
        return if (isOpenAmount && !isAdminFeeIncluded) {
            totalPrice + adminFee
        } else {
            totalPrice
        }
    }

    private fun onSuccessCancelVoucher(defaultErrorMsg: String): (CancelVoucherData.Response) -> Unit {
        return {
            if (it.response.success) {
                setPromoData(PromoData(state = TickerCheckoutView.State.EMPTY, description = ""))
                _cancelVoucherData.postValue(Success(it.response))
            } else {
                _cancelVoucherData.postValue(Fail(MessageErrorException(defaultErrorMsg)))
            }
        }
    }

    private fun onErrorCancelVoucher(defaultErrorMsg: String): (Throwable) -> Unit {
        return {
            if (it.message.isNullOrEmpty()) {
                _cancelVoucherData.postValue(Fail(MessageErrorException(defaultErrorMsg)))
            } else {
                _cancelVoucherData.postValue(Fail(it))
            }
        }
    }

    private fun onReceivedPromoCode() {
        resetCheckoutSummaryPromoAndTotalPrice()

        val promoDataValue = promoData.value ?: PromoData()
        if (promoDataValue.amount > 0) {
            if (promoDataValue.isActive()) {
                paymentSummary.addToSummary(
                    SUMMARY_PROMO_CODE_POSITION,
                    Payment(
                        STRING_KODE_PROMO,
                        String.format("-%s", getStringIdrFormat(promoDataValue.amount.toDouble()))
                    )
                )
            } else {
                // if it is inactive and have promo amount
                paymentSummary.addToSummary(
                    SUMMARY_PROMO_CODE_POSITION,
                    Payment(
                        promoDataValue.description,
                        String.format("-%s", getStringIdrFormat(promoDataValue.amount.toDouble()))
                    )
                )
            }
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

    fun onSubscriptionChecked(
        fintechProduct: FintechProduct,
        isChecked: Boolean,
        consentPayload: String
    ) {
        val digitalCrossSellData = DigitalCrossSellData(fintechProduct, true, consentPayload)
        updateRequestCheckoutParamWithCrossSellData(digitalCrossSellData, isChecked)
        requestCheckoutParam.isSubscriptionChecked = isChecked
    }

    fun onFintechProductChecked(
        fintechProduct: FintechProduct,
        isChecked: Boolean,
        inputPrice: Double?
    ) {
        val digitalCrossSellData = DigitalCrossSellData(fintechProduct, false,"")
        updateRequestCheckoutParamWithCrossSellData(digitalCrossSellData, isChecked)
        updateTotalPriceWithFintechProduct(inputPrice)
        updateCheckoutSummaryWithFintechProduct(fintechProduct, isChecked)
    }

    private fun updateRequestCheckoutParamWithCrossSellData(
        digitalCrossSellData: DigitalCrossSellData,
        isChecked: Boolean
    ) {
        val fintechProduct = digitalCrossSellData.product
        if (requestCheckoutParam.crossSellProducts.containsKey(fintechProduct.tierId) && !isChecked) {
            // remove
            requestCheckoutParam.crossSellProducts.remove(fintechProduct.tierId)
        } else if (!requestCheckoutParam.crossSellProducts.containsKey(fintechProduct.tierId) && isChecked) {
            // add
            requestCheckoutParam.crossSellProducts[fintechProduct.tierId] = digitalCrossSellData
        }
    }

    private fun updateTotalPriceWithFintechProduct(inputPrice: Double?) {
        cartDigitalInfoData.value?.attributes?.let { attributes ->
            var totalPrice = inputPrice ?: attributes.pricePlain

            requestCheckoutParam.crossSellProducts.forEach { crossSell ->
                if (!crossSell.value.isSubscription) {
                    totalPrice += crossSell.value.product.fintechAmount
                }
            }
            _totalPrice.postValue(
                calculateTotalPrice(
                    totalPrice,
                    attributes.adminFee,
                    attributes.isOpenAmount,
                    attributes.isAdminFeeIncluded
                )
            )
        }
    }

    fun updateCheckoutSummaryWithFintechProduct(
        fintechProduct: FintechProduct,
        isChecked: Boolean
    ) {
        if (isChecked) {
            paymentSummary.addToSummary(
                Payment(
                    fintechProduct.transactionType,
                    getStringIdrFormat(fintechProduct.fintechAmount)
                )
            )
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

    fun proceedToCheckout(digitalIdentifierParam: RequestBodyIdentifier, isUseGql: Boolean) {
        val promoCode = promoData.value?.promoCode ?: ""
        val cartDigitalInfoData = _cartDigitalInfoData.value
        cartDigitalInfoData?.let {
            requestCheckoutParam.voucherCode = promoCode

            _showLoading.postValue(true)

            if (requestCheckoutParam.isNeedOtp) {
                _isNeedOtp.postValue(userSession.phoneNumber)
            } else {
                launchCatchError(block = {
                    val checkoutData = withContext(dispatcher) {
                        digitalCheckoutUseCase.execute(
                            requestCheckoutParams = requestCheckoutParam,
                            digitalIdentifierParams = digitalIdentifierParam,
                            fintechProduct = it.attributes.fintechProduct.getOrNull(0),
                            isUseGql = isUseGql
                        )
                    }

                    _paymentPassData.postValue(checkoutData)

                    requestCheckoutParam.crossSellProducts.let { crossSell ->
                        crossSell.values.forEach {
                            if (it.isSubscription) return@forEach
                            if (it.product.info.iconUrl.isNotEmpty()) {
                                analytics.eventProceedCheckoutTebusMurah(
                                    it.product,
                                    cartDigitalInfoData.attributes.categoryName,
                                    userSession.userId
                                )
                            } else {
                                analytics.eventProceedCheckoutCrossell(
                                    it.product,
                                    cartDigitalInfoData.attributes.categoryName,
                                    userSession.userId
                                )
                            }
                        }
                    }
                }) {
                    _showLoading.postValue(false)
                    if (it is ResponseErrorException) {
                        _errorThrowable.postValue(Fail(MessageErrorException(it.message)))
                    } else {
                        _errorThrowable.postValue(Fail(it))
                    }
                }
            }
        }
    }

    fun getPromoDigitalModel(
        cartPassData: DigitalCheckoutPassData?,
        userInputPriceAmount: Double?
    ): PromoDigitalModel {
        val cartInfoData = cartDigitalInfoData.value ?: CartDigitalInfoData()
        var price: Double = cartInfoData.attributes.pricePlain

        if (userInputPriceAmount != null) {
            price = userInputPriceAmount
        }

        return PromoDigitalModel(
            cartPassData?.categoryId?.toIntSafely() ?: 0,
            cartInfoData.attributes.categoryName,
            cartInfoData.attributes.operatorName,
            cartPassData?.productId?.toIntSafely() ?: 0,
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
        } else if (promoData.state == TickerCheckoutView.State.ACTIVE || promoData.state == TickerCheckoutView.State.INACTIVE) {
            onReceivedPromoCode()
        }
    }

    private fun <T> MutableLiveData<T>.forceRefresh() {
        this.value = this.value
    }
}
