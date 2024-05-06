package com.tokopedia.checkoutpayment.topup.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.checkoutpayment.list.view.Failure
import com.tokopedia.checkoutpayment.list.view.OccState
import com.tokopedia.checkoutpayment.topup.data.PaymentCustomerData
import com.tokopedia.checkoutpayment.topup.domain.GetOvoTopUpUrlUseCase
import javax.inject.Inject

class OvoTopUpWebViewViewModel @Inject constructor(private val getOvoTopUpUrlUseCase: GetOvoTopUpUrlUseCase) : ViewModel() {

    private val _ovoTopUpUrl: MutableLiveData<OccState<String>> = MutableLiveData(OccState.Loading)
    val ovoTopUpUrl: LiveData<OccState<String>>
        get() = _ovoTopUpUrl

    fun getOvoTopUpUrl(redirectUrl: String, customerData: PaymentCustomerData) {
        _ovoTopUpUrl.value = OccState.Loading
        getOvoTopUpUrlUseCase.execute(
            customerData.name,
            customerData.email,
            customerData.msisdn,
            redirectUrl,
            { url -> _ovoTopUpUrl.value = OccState.Success(url) },
            { throwable -> _ovoTopUpUrl.value = OccState.Failed(Failure(throwable)) }
        )
    }
}
