package com.tokopedia.thankyou_native.presentation.views.listener

interface HeaderListener {
    fun onCopyAccountId(accountNumberStr: String)
    fun onCopyAmount(amountStr: String)
    fun onSeeDetailInvoice()
    fun onPrimaryButtonClick()
    fun onSecondaryButtonClick()
    fun openApplink(applink: String?)
}
