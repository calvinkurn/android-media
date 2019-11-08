package com.tokopedia.emoney.view.electronicmoney

import com.tokopedia.emoney.data.EmoneyInquiry

interface EmoneyActionListener {

    fun setIssuerId(issuerIdEmoney: Int)
    fun onErrorCardNotFound(issuerIdEmoney: Int)
    fun onError(stringResource: Int)
}

interface BrizziActionListener : EmoneyActionListener {
    fun onSuccess(emoneyInquiry: EmoneyInquiry)
    fun processGetBalanceBrizzi(refresh: Boolean)
    fun logBrizziStatus(firstLogInquiry: Boolean, emoneyInquiry: EmoneyInquiry)
}

interface MandiriActionListener : EmoneyActionListener {
    fun sendCommand(id: Int, mapAttributes: HashMap<String, Any>)
    fun onSuccess(mapAttributes: HashMap<String, Any>)
}