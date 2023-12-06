package com.tokopedia.thankyou_native.presentation.views.listener

interface WaitingHeaderListener {
    fun onCopyAccountId(accountNumberStr: String)
    fun onCopyAmount(amountStr: String)
    fun onSeeDetailInvoice()
    fun onPrimaryButtonClick()
    fun onSecondaryButtonClick()
}
