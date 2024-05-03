package com.tokopedia.checkoutpayment.list.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.checkoutpayment.list.data.ListingParam
import com.tokopedia.checkoutpayment.list.data.PaymentListingParamRequest
import com.tokopedia.checkoutpayment.list.domain.GetPaymentListingParamUseCase
import java.net.URLEncoder
import javax.inject.Inject

class PaymentListingViewModel @Inject constructor(
    private val getPaymentListingParamUseCase: GetPaymentListingParamUseCase
) : ViewModel() {

    private val _paymentListingPayload: MutableLiveData<OccState<String>> = MutableLiveData(OccState.Loading)
    val paymentListingPayload: LiveData<OccState<String>>
        get() = _paymentListingPayload

    fun getPaymentListingPayload(
            request: PaymentListingParamRequest,
            amount: Double,
            orderMetadata: String,
            promoParam: String
    ) {
        _paymentListingPayload.value = OccState.Loading
        getPaymentListingParamUseCase.execute(request, {
            _paymentListingPayload.value = OccState.Success(
                generatePayload(
                    it,
                    request.version,
                    amount,
                    orderMetadata,
                    promoParam
                )
            )
        }, {
            _paymentListingPayload.value = OccState.Failed(Failure(it))
        })
    }

    private fun generatePayload(
        param: ListingParam,
        version: String,
        amount: Double,
        orderMetadata: String,
        promoParam: String
    ): String {
        return "merchant_code=${getUrlEncoded(param.merchantCode)}&" +
            "profile_code=${getUrlEncoded(param.profileCode)}&" +
            "user_id=${getUrlEncoded(param.userId)}&" +
            "customer_name=${getUrlEncoded(param.customerName)}&" +
            "customer_email=${getUrlEncoded(param.customerEmail)}&" +
            "customer_msisdn=${getUrlEncoded(param.customerMsisdn)}&" +
            "address_id=${getUrlEncoded(param.addressId)}&" +
            "callback_url=${getUrlEncoded(param.callbackUrl)}&" +
            "version=${getUrlEncoded(version)}&" +
            "signature=${getUrlEncoded(param.hash)}&" +
            "amount=${getUrlEncoded(amount.toString())}&" +
            "bid=${getUrlEncoded(param.bid)}&" +
            "order_metadata=${getUrlEncoded(orderMetadata)}&" +
            "promo_param=${getUrlEncoded(promoParam)}&" +
            "unique_key=${getUrlEncoded(param.uniqueKey)}"
    }

    private fun getUrlEncoded(valueStr: String): String {
        return URLEncoder.encode(valueStr, "UTF-8")
    }
}

sealed class OccState<out T : Any> {
    data class FirstLoad<out T : Any>(val data: T) : OccState<T>()
    data class Success<out T : Any>(val data: T) : OccState<T>()
    object Loading : OccState<Nothing>()
    data class Failed(private val failure: Failure) : OccState<Nothing>() {
        private val eventFailure: OccEvent<Failure> = OccEvent(failure)
        fun getFailure(): Failure? {
            return eventFailure.getData()
        }
    }
}

data class OccEvent<out T : Any>(private val data: T) {
    private var isConsumed: Boolean = false

    fun getData(): T? {
        if (isConsumed) {
            return null
        }
        isConsumed = true
        return data
    }
}

data class Failure(val throwable: Throwable? = null, val errorMessage: String = "")
