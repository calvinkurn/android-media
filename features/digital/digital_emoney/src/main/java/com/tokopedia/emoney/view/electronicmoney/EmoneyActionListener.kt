package com.tokopedia.emoney.view.electronicmoney

import android.content.Intent
import com.tokopedia.emoney.data.EmoneyInquiry

interface EmoneyActionListener {

    fun setIssuerId(issuerIdEmoney: Int)
    fun onErrorCardNotFound(issuerIdEmoney: Int, intent: Intent)
    fun onErrorDefault(stringResource: Int)
    fun showCardLastBalance(emoneyInquiry: EmoneyInquiry)
}

interface BrizziActionListener : EmoneyActionListener {
    fun executeBrizzi(refresh: Boolean, intent: Intent)
    fun logStatusBrizzi(firstLogInquiry: Boolean, emoneyInquiry: EmoneyInquiry)
}

interface MandiriActionListener : EmoneyActionListener {
    fun sendCommandMandiri(id: Int, mapAttributes: HashMap<String, Any>)
    fun getInquiryBalanceMandiri(mapAttributes: HashMap<String, Any>)
}