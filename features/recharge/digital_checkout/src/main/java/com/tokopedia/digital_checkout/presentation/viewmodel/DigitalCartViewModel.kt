package com.tokopedia.digital_checkout.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigital
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigitalWithTitle
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
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
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

    private val _cartDigitalInfoData = MutableLiveData<CartDigitalInfoData>()
    val cartDigitalInfoData: LiveData<CartDigitalInfoData>
        get() = _cartDigitalInfoData

    private val _cartAdditionalInfoList = MutableLiveData<List<CartDigitalInfoData.CartItemDigitalWithTitle>>()
    val cartAdditionalInfoList: LiveData<List<CartDigitalInfoData.CartItemDigitalWithTitle>>
        get() = _cartAdditionalInfoList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _isNeedOtp = MutableLiveData<Boolean>()
    val isNeedOtp: LiveData<Boolean>
        get() = _isNeedOtp

    private val _isSuccessCancelVoucherCart = MutableLiveData<Result<Boolean>>()
    val isSuccessCancelVoucherCart: LiveData<Result<Boolean>>
        get() = _isSuccessCancelVoucherCart

    private val _totalPrice = MutableLiveData<String>()
    val totalPrice: LiveData<String>
        get() = _totalPrice

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
                val mappedCartData = DigitalCheckoutMapper.mapToCartDigitalInfoData(responseCartData)

                if (mappedCartData.isNeedOtp) {
                    _isNeedOtp.postValue(true)
                } else {
                    _cartDigitalInfoData.postValue(mappedCartData)
                    _cartAdditionalInfoList.postValue(mappedCartData.additionalInfos)
                    _totalPrice.postValue(getStringIdrFormat(mappedCartData.attributes?.pricePlain?.toDouble()
                            ?: 0.0))
                }
            }
        }
    }

    fun cancelVoucherCart() {
        //hit api
        _isSuccessCancelVoucherCart.postValue(Success(true))
//        detailToggleAppCompatTextView.setText(R.string.digital_cart_detail_close_label)
        resetVoucherCart()
    }

    fun onReceivedPromoCode(promoData: PromoData) {
        resetVoucherCart()
        if (promoData.amount == 0) {
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
            _totalPrice.value = getStringIdrFormat(totalPayment.toDouble())
//            getView().expandAdditionalInfo()
        }
    }

    private fun resetVoucherCart() {
        val additionalInfos = cartAdditionalInfoList.value?.toMutableList() ?: mutableListOf()
        for ((i, additionalInfo) in additionalInfos.withIndex()) {
            if (additionalInfo.title.contains("Pembayaran")) {
                additionalInfos.removeAt(i)
                break
            }
        }
        _cartAdditionalInfoList.value = additionalInfos
    }

    private fun getStringIdrFormat(value: Double): String {
        val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
        kursIndonesia.maximumFractionDigits = 0
        val formatRp = DecimalFormatSymbols()
        formatRp.currencySymbol = "Rp "
        formatRp.groupingSeparator = '.'
        formatRp.monetaryDecimalSeparator = '.'
        formatRp.decimalSeparator = '.'
        kursIndonesia.decimalFormatSymbols = formatRp
        return kursIndonesia.format(value).replace(",", ".")
    }
}