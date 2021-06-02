package com.tokopedia.oneclickcheckout.preference.edit.view

interface PreferenceEditParent {

    fun getAddressId(): Long

    fun setHeaderTitle(title: String)
    fun setHeaderSubtitle(subtitle: String)

    fun hideStepper()

    fun hideAddButton()

    fun hideDeleteButton()

    fun getPaymentProfile(): String
    fun getPaymentAmount(): Double

    fun isDirectPaymentStep(): Boolean
}