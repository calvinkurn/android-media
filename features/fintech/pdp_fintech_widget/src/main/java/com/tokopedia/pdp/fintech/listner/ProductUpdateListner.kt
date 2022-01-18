package com.tokopedia.pdp.fintech.listner

interface ProductUpdateListner {

    fun removeWidget()
    fun showWidget()
    fun fintechRedirection(ctaType: Int,linkToRedirect: String)
}