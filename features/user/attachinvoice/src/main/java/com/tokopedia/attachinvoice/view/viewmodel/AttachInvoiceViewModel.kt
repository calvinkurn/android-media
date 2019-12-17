package com.tokopedia.attachinvoice.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.attachinvoice.usecase.GetInvoiceUseCase
import javax.inject.Inject

class AttachInvoiceViewModel @Inject constructor(
        private val getInvoiceUseCase: GetInvoiceUseCase
): ViewModel() {

    fun loadInvoices(page: Int) {

    }

}