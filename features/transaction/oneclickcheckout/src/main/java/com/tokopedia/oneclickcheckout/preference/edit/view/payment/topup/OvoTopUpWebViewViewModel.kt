package com.tokopedia.oneclickcheckout.preference.edit.view.payment.topup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccMutableLiveData
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentOvoCustomerData
import com.tokopedia.oneclickcheckout.preference.edit.domain.payment.GetOvoTopUpUrlUseCase
import javax.inject.Inject

class OvoTopUpWebViewViewModel @Inject constructor(private val getOvoTopUpUrlUseCase: GetOvoTopUpUrlUseCase) : ViewModel() {

    private val _ovoTopUpUrl: OccMutableLiveData<OccState<String>> = OccMutableLiveData(OccState.Loading)
    val ovoTopUpUrl: LiveData<OccState<String>>
        get() = _ovoTopUpUrl

    fun getOvoTopUpUrl(redirectUrl: String, customerData: OrderPaymentOvoCustomerData) {
        _ovoTopUpUrl.value = OccState.Loading
        getOvoTopUpUrlUseCase.execute(customerData.name, customerData.email, customerData.msisdn, redirectUrl,
                { url -> _ovoTopUpUrl.value = OccState.Success(url) },
                { throwable -> _ovoTopUpUrl.value = OccState.Failed(Failure(throwable)) })
    }
}