package com.tokopedia.thankyou_native.presentation.views.listener

interface HeaderListener {
    fun onCopyAccountId(accountNumberStr: String)
    fun onCopyAmount(amountStr: String)
    fun onSeeDetailInvoice()
    fun onPrimaryButtonClick()
    fun onSecondaryButtonClick()
    fun openApplink(applink: String?)

    fun onButtonClick(applink: String, type: String, isPrimary: Boolean, text: String)
}
