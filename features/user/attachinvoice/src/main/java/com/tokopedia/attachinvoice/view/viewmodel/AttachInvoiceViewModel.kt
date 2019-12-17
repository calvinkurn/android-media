package com.tokopedia.attachinvoice.view.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.attachinvoice.usecase.GetInvoiceUseCase
import javax.inject.Inject

class AttachInvoiceViewModel @Inject constructor(
        private val getInvoiceUseCase: GetInvoiceUseCase
): ViewModel() {

    var messageId: String = ""

    fun loadInvoices(page: Int) {

    }

    fun initializeArguments(arguments: Bundle?) {
        if (arguments == null) return
        messageId = arguments.getString(ApplinkConst.AttachInvoice.PARAM_MESSAGE_ID, "")
    }

}