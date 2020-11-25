package com.tokopedia.oneclickcheckout.preference.edit.view.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.preference.edit.data.payment.ListingParam
import com.tokopedia.oneclickcheckout.preference.edit.data.payment.PaymentListingParamRequest
import com.tokopedia.oneclickcheckout.preference.edit.domain.payment.GetPaymentListingParamUseCase
import java.net.URLEncoder
import javax.inject.Inject

class PaymentMethodViewModel @Inject constructor(private val getPaymentListingParamUseCase: GetPaymentListingParamUseCase) : ViewModel() {

    private val _paymentListingPayload: MutableLiveData<OccState<String>> = MutableLiveData(OccState.Loading)
    val paymentListingPayload: LiveData<OccState<String>>
        get() = _paymentListingPayload

    fun getPaymentListingPayload(request: PaymentListingParamRequest, amount: Double) {
        _paymentListingPayload.value = OccState.Loading
        getPaymentListingParamUseCase.execute(request, {
            _paymentListingPayload.value = OccState.Success(generatePayload(it, request.version, amount))
        }, {
            _paymentListingPayload.value = OccState.Failed(Failure(it))
        })
    }

    private fun generatePayload(param: ListingParam, version: String, amount: Double): String {
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
                "amount=${getUrlEncoded(amount.toString())}"
    }

    private fun getUrlEncoded(valueStr: String): String {
        return URLEncoder.encode(valueStr, "UTF-8")
    }
}