package com.tokopedia.oneclickcheckout.preference.edit.view.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.preference.edit.data.payment.ListingParam
import com.tokopedia.oneclickcheckout.preference.edit.data.payment.PaymentListingParamRequest
import com.tokopedia.oneclickcheckout.preference.edit.domain.payment.GetPaymentListingParamUseCase
import javax.inject.Inject

class PaymentMethodViewModel @Inject constructor(private val getPaymentListingParamUseCase: GetPaymentListingParamUseCase) : ViewModel() {

    private val _paymentListingParam: MutableLiveData<OccState<ListingParam>> = MutableLiveData(OccState.Loading)
    val paymentListingParam: LiveData<OccState<ListingParam>>
        get() = _paymentListingParam

    fun getPaymentListingParam(request: PaymentListingParamRequest) {
        _paymentListingParam.value = OccState.Loading
        getPaymentListingParamUseCase.execute(request, {
            _paymentListingParam.value = OccState.Success(it)
        }, {
            _paymentListingParam.value = OccState.Failed(Failure(it))
        })
    }
}